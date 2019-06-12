////////////////////////////////////////////////////////////////////////////////
///
/// Example class that invokes native SoundTouch routines through the JNI
/// interface.
///
/// Author        : Copyright (c) Olli Parviainen
/// Author e-mail : oparviai 'at' iki.fi
/// WWW           : http://www.surina.net
///
////////////////////////////////////////////////////////////////////////////////

package com.madv.soundtouch;

public final class SoundTouch {
    // Native interface function that returns SoundTouch version string.
    // This invokes the native c++ routine defined in "soundtouch-jni.cpp".
    public native final static String getVersionString();

    public native final static String getErrorString();

    private native final static long newInstance();

    private native final void deleteInstance(long handle);

    /// Sets new rate control value. Normal rate = 1.0, smaller values
    /// represent slower rate, larger faster rates.
    private native final void setRate(long handle, float rate);

    /// Sets new tempo control value. Normal tempo = 1.0, smaller values
    /// represent slower tempo, larger faster tempo.
    private native final void setTempo(long handle, float tempo);

    /// Sets new rate control value as a difference in percents compared
    /// to the original rate (-50 .. +100 %)
    private native final void setRateChange(long handle, float rate);

    /// Sets new tempo control value as a difference in percents compared
    /// to the original tempo (-50 .. +100 %)
    private native final void setTempoChange(long handle, float tempo);

    /// Sets new pitch control value. Original pitch = 1.0, smaller values
    /// represent lower pitches, larger values higher pitch.
    private native final void setPitch(long handle, double pitch);

    /// Sets pitch change in octaves compared to the original pitch
    /// (-1.00 .. +1.00)
    private native final void setPitchOctaves(long handle, float pitch);

    /// Sets pitch change in semi-tones compared to the original pitch
    /// (-12 .. +12)
    private native final void setPitchSemiTones(long handle, float pitch);

    private native final int processFile(long handle, String inputFile, String outputFile);

    private native final void setSampleRate(long handle, int srate);

    private native final void setChannels(long handle, int numChannels);

    private native final void putSamples(long handle, byte[] sampleBuffer, int numSamples);

    private native final int receiveSamples(long handle, byte[] sampleBuffer, int maxSamples);

    private native final void flush(long handle);

    long handle = 0;

    public SoundTouch() {
        handle = newInstance();
    }

    public void close() {
        deleteInstance(handle);
        handle = 0;
    }

    /// Sets new rate control value. Normal rate = 1.0, smaller values
    /// represent slower rate, larger faster rates.
    public void setRate(float rate) {
        setRate(handle, rate);
    }

    /// Sets new tempo control value. Normal tempo = 1.0, smaller values
    /// represent slower tempo, larger faster tempo.
    public void setTempo(float tempo) {
        setTempo(handle, tempo);
    }

    /// Sets new rate control value as a difference in percents compared
    /// to the original rate (-50 .. +100 %)
    public void setRateChange(float rate) {
        setRateChange(handle, rate);
    }

    /// Sets new tempo control value as a difference in percents compared
    /// to the original tempo (-50 .. +100 %)
    public void setTempoChange(float tempo) {
        setTempoChange(handle, tempo);
    }

    /// Sets new pitch control value. Original pitch = 1.0, smaller values
    /// represent lower pitches, larger values higher pitch.
    public void setPitch(float pitch) {
        setRate(handle, pitch);
    }

    /// Sets pitch change in octaves compared to the original pitch
    /// (-1.00 .. +1.00)
    public void setPitchOctaves(float pitch) {
        setPitchOctaves(handle, pitch);
    }

    /// Sets pitch change in octaves compared to the original pitch
    /// (-1.00 .. +1.00)
    public void setPitchSemiTones(float pitch) {
        setPitchSemiTones(handle, pitch);
    }

    public int processFile(String inputFile, String outputFile) {
        return processFile(handle, inputFile, outputFile);
    }

    public void setSampleRate(int srate) {
        setSampleRate(handle, srate);
    }

    public void setChannels(int numChannels) {
        setChannels(handle, numChannels);
    }

    public void putSamples(byte[] sampleBuffer, int numSamples) {
        putSamples(handle, sampleBuffer, numSamples);
    }

    public int receiveSamples(byte[] sampleBuffer, int maxSamples) {
        return receiveSamples(handle, sampleBuffer, maxSamples);
    }

    public void flush() {
        flush(handle);
    }

    // Load the native library upon startup
    static {
        System.loadLibrary("soundtouch");
    }
}
