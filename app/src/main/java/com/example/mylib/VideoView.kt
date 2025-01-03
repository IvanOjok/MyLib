package com.example.mylib

import android.R
import android.content.Context
import android.content.Intent
import android.graphics.SurfaceTexture
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.*
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.MediaController
import android.widget.MediaController.MediaPlayerControl
import java.io.IOException


class VideoView : TextureView, MediaPlayerControl {
    // currentState is a VideoView object's current state.
    // targetState is the state that a method caller intends to reach.
    // For instance, regardless the VideoView object's current state,
    // calling pause() intends to bring the object to a target state
    // of STATE_PAUSED.
    private var mCurrentState: Int = VideoView.Companion.STATE_IDLE
    private var mTargetState: Int = VideoView.Companion.STATE_IDLE

    // Stuff we need for playing and showing a video
    private var mMediaPlayer: MediaPlayer? = null
    private var mVideoWidth = 0
    private var mVideoHeight = 0
    private var mSurfaceWidth = 0
    private var mSurfaceHeight = 0
    private var mSurfaceTexture: SurfaceTexture? = null
    private var mSurface: Surface? = null
    private var mMediaController: MediaController? = null
    private var mOnCompletionListener: OnCompletionListener? = null
    private var mOnPreparedListener: OnPreparedListener? = null
    private var mOnErrorListener: OnErrorListener? = null
    private var mOnInfoListener: OnInfoListener? = null
    private var mSeekWhenPrepared // recording the seek position while
            = 0

    // preparing
    private var mCurrentBufferPercentage = 0
    private var mAudioSession = 0
    private var mUri: Uri? = null
    private var mContext: Context

    constructor(context: Context) : super(context) {
        mContext = context
        initVideoView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        mContext = context
        initVideoView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        mContext = context
        initVideoView()
    }

    fun initVideoView() {
        mVideoHeight = 0
        mVideoWidth = 0
  //      setBackgroundColor(resources.getColor(R.color.transparent))
        isFocusable = false
        surfaceTextureListener = mSurfaceTextureListener
    }

    fun resolveAdjustedSize(desiredSize: Int, measureSpec: Int): Int {
        var result = desiredSize
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        when (specMode) {
            MeasureSpec.UNSPECIFIED ->            /*
			 * Parent says we can be as big as we want. Just don't be larger
			 * than max size imposed on ourselves.
			 */result = desiredSize
            MeasureSpec.AT_MOST ->            /*
			 * Parent says we can be as big as we want, up to specSize. Don't be
			 * larger than specSize, and don't be larger than the max size
			 * imposed on ourselves.
			 */result = Math.min(desiredSize, specSize)
            MeasureSpec.EXACTLY ->            // No choice. Do what we are told.
                result = specSize
        }
        return result
    }

    fun setVideoPath(path: String) {
        Log.d(VideoView.Companion.TAG, "Setting video path to: $path")
        setVideoURI(Uri.parse(path))
    }

    fun setVideoURI(_videoURI: Uri?) {
        mUri = _videoURI
        mSeekWhenPrepared = 0
        requestLayout()
        invalidate()
        openVideo()
    }

    val uri: Uri?
        get() = mUri

    override fun setSurfaceTexture(_surfaceTexture: SurfaceTexture) {
        mSurfaceTexture = _surfaceTexture
    }

    fun openVideo() {
        if (mUri == null || mSurfaceTexture == null) {
            Log.d(VideoView.Companion.TAG, "Cannot open video, uri or surface texture is null.")
            return
        }
        // Tell the music playback service to pause
        // TODO: these constants need to be published somewhere in the
        // framework.
        val i = Intent("com.android.music.musicservicecommand")
        i.putExtra("command", "pause")
        mContext.sendBroadcast(i)
        release(false)
        try {
            mSurface = Surface(mSurfaceTexture)
            mMediaPlayer = MediaPlayer()
            if (mAudioSession != 0) {
                mMediaPlayer!!.audioSessionId = mAudioSession
            } else {
                mAudioSession = mMediaPlayer!!.audioSessionId
            }
            mMediaPlayer!!.setOnBufferingUpdateListener(mBufferingUpdateListener)
            mMediaPlayer!!.setOnCompletionListener(mCompleteListener)
            mMediaPlayer!!.setOnPreparedListener(mPreparedListener)
            mMediaPlayer!!.setOnErrorListener(mErrorListener)
            mMediaPlayer!!.setOnInfoListener(mOnInfoListener)
            mMediaPlayer!!.setOnVideoSizeChangedListener(mVideoSizeChangedListener)
            mMediaPlayer!!.setSurface(mSurface)
            mCurrentBufferPercentage = 0
            mMediaPlayer!!.setDataSource(mContext, mUri!!)
            mMediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mMediaPlayer!!.setScreenOnWhilePlaying(true)
            mMediaPlayer!!.prepareAsync()
            mCurrentState = VideoView.Companion.STATE_PREPARING
        } catch (e: IllegalStateException) {
            mCurrentState = VideoView.Companion.STATE_ERROR
            mTargetState = VideoView.Companion.STATE_ERROR
            Log.d(VideoView.Companion.TAG, e.message!!) // TODO auto-generated catch block
        } catch (e: IOException) {
            mCurrentState = VideoView.Companion.STATE_ERROR
            mTargetState = VideoView.Companion.STATE_ERROR
            Log.d(VideoView.Companion.TAG, e.message!!) // TODO auto-generated catch block
        }
    }

    fun stopPlayback() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null
            if (null != mMediaControllListener) {
                mMediaControllListener!!.onStop()
            }
        }
    }

    fun setMediaController(controller: MediaController?) {
        if (mMediaController != null) {
            mMediaController!!.hide()
        }
        mMediaController = controller
        attachMediaController()
    }

    private fun attachMediaController() {
        if (mMediaPlayer != null && mMediaController != null) {
            mMediaController!!.setMediaPlayer(this)
            val anchorView: View = if (this.parent is View) this.parent as View else this
            mMediaController!!.setAnchorView(anchorView)
            mMediaController!!.setEnabled(isInPlaybackState)
        }
    }

    private fun release(cleartargetstate: Boolean) {
        Log.d(VideoView.Companion.TAG, "Releasing media player.")
        if (mMediaPlayer != null) {
            mMediaPlayer!!.reset()
            mMediaPlayer!!.release()
            mMediaPlayer = null
            mCurrentState = VideoView.Companion.STATE_IDLE
            if (cleartargetstate) {
                mTargetState = VideoView.Companion.STATE_IDLE
            }
        } else {
            Log.d(VideoView.Companion.TAG, "Media player was null, did not release.")
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // Will resize the view if the video dimensions have been found.
        // video dimensions are found after onPrepared has been called by
        // MediaPlayer
        var width = getDefaultSize(mVideoWidth, widthMeasureSpec)
        var height = getDefaultSize(mVideoHeight, heightMeasureSpec)
        if (mVideoWidth > 0 && mVideoHeight > 0) {
            if (mVideoWidth * height > width * mVideoHeight) {
                Log.d(VideoView.Companion.TAG, "Video too tall, change size.")
                height = width * mVideoHeight / mVideoWidth
            } else if (mVideoWidth * height < width * mVideoHeight) {
                Log.d(VideoView.Companion.TAG, "Video too wide, change size.")
                width = height * mVideoWidth / mVideoHeight
            } else {
                Log.d(VideoView.Companion.TAG, "Aspect ratio is correct.")
            }
        }
        setMeasuredDimension(width, height)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (isInPlaybackState && mMediaController != null) {
            toggleMediaControlsVisiblity()
        }
        return false
    }

    override fun onTrackballEvent(ev: MotionEvent): Boolean {
        if (isInPlaybackState && mMediaController != null) {
            toggleMediaControlsVisiblity()
        }
        return false
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val isKeyCodeSupported =
            keyCode != KeyEvent.KEYCODE_BACK && keyCode != KeyEvent.KEYCODE_VOLUME_UP && keyCode != KeyEvent.KEYCODE_VOLUME_DOWN && keyCode != KeyEvent.KEYCODE_VOLUME_MUTE && keyCode != KeyEvent.KEYCODE_MENU && keyCode != KeyEvent.KEYCODE_CALL && keyCode != KeyEvent.KEYCODE_ENDCALL
        if (isInPlaybackState && isKeyCodeSupported && mMediaController != null) {
            if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
                if (mMediaPlayer!!.isPlaying) {
                    pause()
                    mMediaController!!.show()
                } else {
                    start()
                    mMediaController!!.hide()
                }
                return true
            } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
                if (!mMediaPlayer!!.isPlaying) {
                    start()
                    mMediaController!!.hide()
                }
                return true
            } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
                if (mMediaPlayer!!.isPlaying) {
                    pause()
                    mMediaController!!.show()
                }
                return true
            } else {
                toggleMediaControlsVisiblity()
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun toggleMediaControlsVisiblity() {
        if (mMediaController!!.isShowing()) {
            mMediaController!!.hide()
        } else {
            mMediaController!!.show()
        }
    }

    override fun start() {
        // This can potentially be called at several points, it will go through
        // when all conditions are ready
        // 1. When setting the video URI
        // 2. When the surface becomes available
        // 3. From the activity
        if (isInPlaybackState) {
            mMediaPlayer!!.start()
            mCurrentState = VideoView.Companion.STATE_PLAYING
            if (null != mMediaControllListener) {
                mMediaControllListener!!.onStart()
            }
        } else {
            Log.d(VideoView.Companion.TAG, "Could not start. Current state $mCurrentState")
        }
        mTargetState = VideoView.Companion.STATE_PLAYING
    }

    override fun pause() {
        if (isInPlaybackState) {
            if (mMediaPlayer!!.isPlaying) {
                mMediaPlayer!!.pause()
                mCurrentState = VideoView.Companion.STATE_PAUSED
                if (null != mMediaControllListener) {
                    mMediaControllListener!!.onPause()
                }
            }
        }
        mTargetState = VideoView.Companion.STATE_PAUSED
    }

    fun suspend() {
        release(false)
    }

    fun resume() {
        openVideo()
    }

    override fun getDuration(): Int {
        return if (isInPlaybackState) {
            mMediaPlayer!!.duration
        } else -1
    }

    override fun getCurrentPosition(): Int {
        return if (isInPlaybackState) {
            mMediaPlayer!!.currentPosition
        } else 0
    }

    override fun seekTo(msec: Int) {
        mSeekWhenPrepared = if (isInPlaybackState) {
            mMediaPlayer!!.seekTo(msec)
            0
        } else {
            msec
        }
    }

    override fun isPlaying(): Boolean {
        return isInPlaybackState && mMediaPlayer!!.isPlaying
    }

    override fun getBufferPercentage(): Int {
        return if (mMediaPlayer != null) {
            mCurrentBufferPercentage
        } else 0
    }

    private val isInPlaybackState: Boolean
        private get() = mMediaPlayer != null && mCurrentState != VideoView.Companion.STATE_ERROR && mCurrentState != VideoView.Companion.STATE_IDLE && mCurrentState != VideoView.Companion.STATE_PREPARING

    override fun canPause(): Boolean {
        return false
    }

    override fun canSeekBackward(): Boolean {
        return false
    }

    override fun canSeekForward(): Boolean {
        return false
    }

    override fun getAudioSessionId(): Int {
        if (mAudioSession == 0) {
            val foo = MediaPlayer()
            mAudioSession = foo.audioSessionId
            foo.release()
        }
        return mAudioSession
    }

    // Listeners
    private val mBufferingUpdateListener =
        OnBufferingUpdateListener { mp, percent -> mCurrentBufferPercentage = percent }
    private val mCompleteListener =
        OnCompletionListener { mp ->
            mCurrentState = VideoView.Companion.STATE_PLAYBACK_COMPLETED
            mTargetState = VideoView.Companion.STATE_PLAYBACK_COMPLETED
            mSurface!!.release()
            if (mMediaController != null) {
                mMediaController!!.hide()
            }
            if (mOnCompletionListener != null) {
                mOnCompletionListener!!.onCompletion(mp)
            }
            if (mMediaControllListener != null) {
                mMediaControllListener!!.onComplete()
            }
        }
    private val mPreparedListener =
        OnPreparedListener { mp ->
            mCurrentState = VideoView.Companion.STATE_PREPARED
            if (mOnPreparedListener != null) {
                mOnPreparedListener!!.onPrepared(mMediaPlayer)
            }
            if (mMediaController != null) {
                mMediaController!!.setEnabled(true)
            }
            mVideoWidth = mp.videoWidth
            mVideoHeight = mp.videoHeight
            val seekToPosition = mSeekWhenPrepared // mSeekWhenPrepared may be
            // changed after seekTo()
            // call
            if (seekToPosition != 0) {
                seekTo(seekToPosition)
            }
            requestLayout()
            invalidate()
            if (mVideoWidth != 0 && mVideoHeight != 0) {
                if (mTargetState == VideoView.Companion.STATE_PLAYING) {
                    mMediaPlayer!!.start()
                    if (null != mMediaControllListener) {
                        mMediaControllListener!!.onStart()
                    }
                }
            } else {
                if (mTargetState == VideoView.Companion.STATE_PLAYING) {
                    mMediaPlayer!!.start()
                    if (null != mMediaControllListener) {
                        mMediaControllListener!!.onStart()
                    }
                }
            }
        }
    private val mVideoSizeChangedListener =
        OnVideoSizeChangedListener { mp, width, height ->
            mVideoWidth = mp.videoWidth
            mVideoHeight = mp.videoHeight
            if (mVideoWidth != 0 && mVideoHeight != 0) {
                requestLayout()
            }
        }
    private val mErrorListener =
        OnErrorListener { mp, what, extra ->
            Log.d(VideoView.Companion.TAG, "Error: $what,$extra")
            mCurrentState = VideoView.Companion.STATE_ERROR
            mTargetState = VideoView.Companion.STATE_ERROR
            if (mMediaController != null) {
                mMediaController!!.hide()
            }

            /* If an error handler has been supplied, use it and finish. */if (mOnErrorListener != null) {
            if (mOnErrorListener!!.onError(mMediaPlayer, what, extra)) {
                return@OnErrorListener true
            }
        }

            /*
                 * Otherwise, pop up an error dialog so the user knows that
                 * something bad has happened. Only try and pop up the dialog if
                 * we're attached to a window. When we're going away and no longer
                 * have a window, don't bother showing the user an error.
                 */if (windowToken != null) {

            //				new AlertDialog.Builder(mContext).setMessage("Error: " + what + "," + extra).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            //					public void onClick(DialogInterface dialog, int whichButton) {
            //						/*
            //						 * If we get here, there is no onError listener, so at
            //						 * least inform them that the video is over.
            //						 */
            //						if (mOnCompletionListener != null) {
            //							mOnCompletionListener.onCompletion(mMediaPlayer);
            //						}
            //					}
            //				}).setCancelable(false).show();
        }
            true
        }
    var mSurfaceTextureListener: SurfaceTextureListener = object : SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
            Log.d(VideoView.Companion.TAG, "onSurfaceTextureAvailable.")
            mSurfaceTexture = surface
            openVideo()
        }

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
            Log.d(VideoView.Companion.TAG, "onSurfaceTextureSizeChanged: $width/$height")
            mSurfaceWidth = width
            mSurfaceHeight = height
            val isValidState = mTargetState == VideoView.Companion.STATE_PLAYING
            val hasValidSize = mVideoWidth == width && mVideoHeight == height
            if (mMediaPlayer != null && isValidState && hasValidSize) {
                if (mSeekWhenPrepared != 0) {
                    seekTo(mSeekWhenPrepared)
                }
                start()
            }
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
            mSurface = null
            if (mMediaController != null) mMediaController!!.hide()
            release(true)
            return true
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
    }

    /**
     * Register a callback to be invoked when the media file is loaded and ready
     * to go.
     *
     * @param l
     * The callback that will be run
     */
    fun setOnPreparedListener(l: OnPreparedListener?) {
        mOnPreparedListener = l
    }

    /**
     * Register a callback to be invoked when the end of a media file has been
     * reached during playback.
     *
     * @param l
     * The callback that will be run
     */
    fun setOnCompletionListener(l: OnCompletionListener?) {
        mOnCompletionListener = l
    }

    /**
     * Register a callback to be invoked when an error occurs during playback or
     * setup. If no listener is specified, or if the listener returned false,
     * VideoView will inform the user of any errors.
     *
     * @param l
     * The callback that will be run
     */
    fun setOnErrorListener(l: OnErrorListener?) {
        mOnErrorListener = l
    }

    /**
     * Register a callback to be invoked when an informational event occurs
     * during playback or setup.
     *
     * @param l
     * The callback that will be run
     */
    fun setOnInfoListener(l: OnInfoListener?) {
        mOnInfoListener = l
    }

    interface MediaControllListener {
        fun onStart()
        fun onPause()
        fun onStop()
        fun onComplete()
    }

    var mMediaControllListener: VideoView.MediaControllListener? = null
    fun setMediaControllListener(mediaControllListener: VideoView.MediaControllListener?) {
        mMediaControllListener = mediaControllListener
    }

    override fun setVisibility(visibility: Int) {
        println("setVisibility: $visibility")
        super.setVisibility(visibility)
    }

    companion object {
        private const val TAG = "VideoView"

        // all possible internal states
        private const val STATE_ERROR = -1
        private const val STATE_IDLE = 0
        private const val STATE_PREPARING = 1
        private const val STATE_PREPARED = 2
        private const val STATE_PLAYING = 3
        private const val STATE_PAUSED = 4
        private const val STATE_PLAYBACK_COMPLETED = 5
    }
}