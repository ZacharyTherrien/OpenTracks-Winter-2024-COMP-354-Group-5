/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package de.dennisguse.opentracks.io.file.exporter;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.PowerManager.WakeLock;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.dennisguse.opentracks.R;
import de.dennisguse.opentracks.content.ContentProviderUtils;
import de.dennisguse.opentracks.content.Track;
import de.dennisguse.opentracks.content.TracksColumns;
import de.dennisguse.opentracks.io.file.TrackFileFormat;
import de.dennisguse.opentracks.util.FileUtils;
import de.dennisguse.opentracks.util.PreferencesUtils;
import de.dennisguse.opentracks.util.SystemUtils;

/**
 * Async Task to save tracks to the external storage.
 *
 * @author Jimmy Shih
 */
//TODO Make independent from ExportActivity?
public class SaveAsyncTask extends AsyncTask<Void, Integer, Boolean> {

    private static final String TAG = SaveAsyncTask.class.getSimpleName();
    private final TrackFileFormat trackFileFormat;
    private final File directory;
    private final Context context;
    private final ContentProviderUtils contentProviderUtils;
    private ExportActivity exportActivity;
    private WakeLock wakeLock;

    // true if the AsyncTask has completed
    private boolean completed;

    // the number of tracks successfully saved
    private int successCount;

    // the number of tracks to save
    private int totalCount;

    // the last successfully saved path
    private String savedPath;

    /**
     * Creates an AsyncTask.
     *
     * @param exportActivity  the activity currently associated with this task
     * @param trackFileFormat the track file format
     * @param directory       the directory to write the file
     */
    public SaveAsyncTask(ExportActivity exportActivity, TrackFileFormat trackFileFormat, File directory) {
        this.exportActivity = exportActivity;
        this.trackFileFormat = trackFileFormat;
        this.directory = directory;
        context = exportActivity.getApplicationContext();
        contentProviderUtils = ContentProviderUtils.Factory.get(context);

        completed = false;
        successCount = 0;
        totalCount = 0;
        savedPath = null;
    }

    /**
     * Sets the current activity associated with this AsyncTask.
     *
     * @param exportActivity the current activity, can be null
     */
    public void setActivity(ExportActivity exportActivity) {
        this.exportActivity = exportActivity;
        if (completed && exportActivity != null) {
            exportActivity.onAsyncTaskCompleted(successCount, totalCount);
        }
    }

    @Override
    protected void onPreExecute() {
        if (exportActivity != null) {
            exportActivity.showProgressDialog();
        }
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
            boolean isRecording = PreferencesUtils.getLong(exportActivity, R.string.recording_track_id_key) != PreferencesUtils.RECORDING_TRACK_ID_DEFAULT;
            boolean isPaused = PreferencesUtils.getBoolean(exportActivity, R.string.recording_track_paused_key, PreferencesUtils.RECORDING_TRACK_PAUSED_DEFAULT);
            // Get the wake lock if not recording or paused
            if (!isRecording || isPaused) {
                wakeLock = SystemUtils.acquireWakeLock(exportActivity, wakeLock);
            }
            return saveAllTracks();
        } finally {
            if (wakeLock != null && wakeLock.isHeld()) {
                wakeLock.release();
            }
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (exportActivity != null) {
            exportActivity.setProgressDialogValue(values[0], values[1]);
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        completed = true;
        if (exportActivity != null) {
            exportActivity.onAsyncTaskCompleted(successCount, totalCount);
        }
    }

    @Override
    protected void onCancelled() {
        completed = true;
        if (exportActivity != null) {
            exportActivity.onAsyncTaskCompleted(successCount, totalCount);
        }
    }

    /**
     * Saves tracks to one file (uses first track to determine the filename).
     *
     * @param tracks the tracks
     */
    private Boolean saveTracks(Track[] tracks) {
        if (tracks.length == 0) {
            return false;
        }

        TrackExporterListener trackExporterListener = new TrackExporterListener() {
            @Override
            public void onProgressUpdate(int number, int max) {
                //Update the progress dialog once every 500 points.
                if (number % 500 == 0) {
                    publishProgress(number, max);
                }
            }
        };

        TrackExporter trackExporter = trackFileFormat.newTrackExporter(context, tracks, trackExporterListener);

        Track track = tracks[0];
        String fileName = FileUtils.buildUniqueFileName(directory, track.getName(), trackFileFormat.getExtension());
        File file = new File(directory, fileName);

        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            if (trackExporter.writeTrack(fileOutputStream)) {
                savedPath = file.getAbsolutePath();
                return true;
            } else {
                if (!file.delete()) {
                    Log.d(TAG, "Unable to delete file");
                }
                Log.e(TAG, "Unable to export track");
                return false;
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Unable to open file " + file.getName(), e);
            return false;
        } catch (IOException e) {
            Log.e(TAG, "Unable to close file output stream", e);
            return false;
        }
    }

    /**
     * Saves all the tracks.
     */
    private Boolean saveAllTracks() {
        try (Cursor cursor = contentProviderUtils.getTrackCursor(null, null, TracksColumns._ID)) {
            if (cursor == null) {
                return false;
            }
            totalCount = cursor.getCount();
            for (int i = 0; i < totalCount; i++) {
                if (isCancelled()) {
                    return false;
                }
                cursor.moveToPosition(i);
                Track track = contentProviderUtils.createTrack(cursor);
                if (track != null && saveTracks(new Track[]{track})) {
                    successCount++;
                }
                publishProgress(i + 1, totalCount);
            }
            return true;
        }
    }
}
