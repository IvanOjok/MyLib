package com.example.mylib.model

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import java.lang.reflect.ParameterizedType


abstract class FirebaseRecyclerAdapter<ViewHolder : RecyclerView.ViewHolder?, T>(
    query: Query,
    items: ArrayList<T>,
    keys: ArrayList<String>
) :
    RecyclerView.Adapter<ViewHolder>() {
    private val mQuery: Query

    /**
     * Returns the list of items of the adapter: can be useful when dealing with a configuration
     * change (e.g.: a device rotation).
     * Just save this list before destroying the adapter and pass it to the new adapter (in the
     * constructor).
     *
     * @return the list of items of the adapter
     */
    var items: ArrayList<T>? = null

    /**
     * Returns the list of keys of the items of the adapter: can be useful when dealing with a
     * configuration change (e.g.: a device rotation).
     * Just save this list before destroying the adapter and pass it to the new adapter (in the
     * constructor).
     *
     * @return the list of keys of the items of the adapter
     */
    var keys: ArrayList<String>? = null

    /**
     * @param query The Firebase location to watch for data changes.
     * Can also be a slice of a location, using some combination of
     * `limit()`, `startAt()`, and `endAt()`.
     */
    //constructor(query: Query) : this(query, items, keys) {}

    private val mListener: ChildEventListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
            val key = dataSnapshot.key
            if (!keys.contains(key)) {
                val item = getConvertedObject(dataSnapshot)
                val insertedPosition: Int
                insertedPosition = if (previousChildName == null) {
                    items.add(0, item!!)
                    keys.add(0, key!!)
                    0
                } else {
                    val previousIndex = keys.indexOf(previousChildName)
                    val nextIndex = previousIndex + 1
                    if (nextIndex == items.size) {
                        items.add(item!!)
                        keys.add(key!!)
                    } else {
                        items.add(nextIndex, item!!)
                        keys.add(nextIndex, key!!)
                    }
                    nextIndex
                }
                notifyItemInserted(insertedPosition)
                itemAdded(item, key, insertedPosition)
            }
        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {
            val key = dataSnapshot.key
            if (keys.contains(key)) {
                val index = keys.indexOf(key)
                val oldItem: T? = items[index]
                val newItem = getConvertedObject(dataSnapshot)
                items.set(index, newItem!!)
                notifyItemChanged(index)
                itemChanged(oldItem, newItem, key, index)
            }
        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            val key = dataSnapshot.key
            if (keys.contains(key)) {
                val index = keys.indexOf(key)
                val item: T? = items[index]
                keys.removeAt(index)
                items.removeAt(index)
                notifyItemRemoved(index)
                itemRemoved(item, key, index)
            }
        }

        override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
            val key = dataSnapshot.key
            val index = keys.indexOf(key)
            val item = getConvertedObject(dataSnapshot)
            items.removeAt(index)
            keys.removeAt(index)
            val newPosition: Int
            newPosition = if (previousChildName == null) {
                items.add(0, item!!)
                keys.add(0, key!!)
                0
            } else {
                val previousIndex = keys.indexOf(previousChildName)
                val nextIndex = previousIndex + 1
                if (nextIndex == items.size) {
                    items.add(item!!)
                    keys.add(key!!)
                } else {
                    items.add(nextIndex, item!!)
                    keys.add(nextIndex, key!!)
                }
                nextIndex
            }
            notifyItemMoved(index, newPosition)
            itemMoved(item, key, index, newPosition)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.e("FirebaseListAdapter", "Listen was cancelled, no more updates will occur.")
        }
    }

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    abstract override fun onBindViewHolder(holder: ViewHolder, position: Int)

    override fun getItemCount(): Int {
        return if (items != null) items!!.size else 0
    }

    /**
     * Clean the adapter.
     * ALWAYS call this method before destroying the adapter to remove the listener.
     */
    fun destroy() {
        mQuery.removeEventListener(mListener)
    }

    /**
     * Returns the item in the specified position
     *
     * @param position Position of the item in the adapter
     * @return the item
     */
    fun getItem(position: Int): T? {
        return items!![position]
    }

    /**
     * Returns the position of the item in the adapter
     *
     * @param item Item to be searched
     * @return the position in the adapter if found, -1 otherwise
     */
    fun getPositionForItem(item: T): Int {
        return if (items != null && items!!.size > 0) items!!.indexOf(item) else -1
    }

    /**
     * Check if the searched item is in the adapter
     *
     * @param item Item to be searched
     * @return true if the item is in the adapter, false otherwise
     */
    operator fun contains(item: T): Boolean {
        return items != null && items!!.contains(item)
    }
    /**
     * ABSTRACT METHODS THAT MUST BE IMPLEMENTED BY THE EXTENDING ADAPTER.
     */
    /**
     * Called after an item has been added to the adapter
     *
     * @param item     Added item
     * @param key      Key of the added item
     * @param position Position of the added item in the adapter
     */
    protected fun itemAdded(item: T?, key: String?, position: Int) {}

    /**
     * Called after an item changed
     *
     * @param oldItem  Old version of the changed item
     * @param newItem  Current version of the changed item
     * @param key      Key of the changed item
     * @param position Position of the changed item in the adapter
     */
    protected fun itemChanged(oldItem: T?, newItem: T?, key: String?, position: Int) {}

    /**
     * Called after an item has been removed from the adapter
     *
     * @param item     Removed item
     * @param key      Key of the removed item
     * @param position Position of the removed item in the adapter
     */
    protected fun itemRemoved(item: T?, key: String?, position: Int) {}

    /**
     * Called after an item changed position
     *
     * @param item        Moved item
     * @param key         Key of the moved item
     * @param oldPosition Old position of the changed item in the adapter
     * @param newPosition New position of the changed item in the adapter
     */
    protected fun itemMoved(item: T?, key: String?, oldPosition: Int, newPosition: Int) {}

    /**
     * Converts the data snapshot to generic object
     *
     * @param snapshot Result
     * @return Data converted
     */
    protected fun getConvertedObject(snapshot: DataSnapshot): T? {
        return snapshot.getValue(genericClass)
    }

    /**
     * Returns a class reference from generic T.
     */
    private val genericClass: Class<T>
        private get() = (this.javaClass.genericSuperclass as ParameterizedType).getActualTypeArguments()
            .get(1) as Class<T>

    /**
     * @param query The Firebase location to watch for data changes.
     * Can also be a slice of a location, using some combination of
     * `limit()`, `startAt()`, and `endAt()`.
     * @param items List of items that will load the adapter before starting the listener.
     * Generally null or empty, but this can be useful when dealing with a
     * configuration change (e.g.: reloading the adapter after a device rotation).
     * Be careful: keys must be coherent with this list.
     * @param keys  List of keys of items that will load the adapter before starting the listener.
     * Generally null or empty, but this can be useful when dealing with a
     * configuration change (e.g.: reloading the adapter after a device rotation).
     * Be careful: items must be coherent with this list.
     */
    init {
        mQuery = query
        if (items != null && keys != null) {
            this.items = items
            this.keys = keys
        } else {
            this.items = ArrayList()
            this.keys = ArrayList()
        }
        query.addChildEventListener(mListener)
    }
}
