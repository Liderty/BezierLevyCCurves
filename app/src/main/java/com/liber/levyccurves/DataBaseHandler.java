package com.liber.levyccurves;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DataBaseHandler extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "curvesDatabase";
    private static final int DATABASE_VERSION = 2;

    // Table Names
    private static final String TABLE_CURVES = "curves";

    // Table Columns
    private static final String KEY_CURVE_ID = "id";
    private static final String KEY_CURVE_N = "curveId";
    private static final String KEY_CURVE_X = "curveX";
    private static final String KEY_CURVE_Y = "curveY";
    private static final String KEY_CURVE_ROTATION = "curveRotation";
    private static final String KEY_CURVE_COLOR = "curveColor";
    private static final String KEY_CURVE_LINE_LENGTH = "curveLineLength";
    private static final String KEY_CURVE_WIDTH = "curveWidth";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CURVES_TABLE = "CREATE TABLE " + TABLE_CURVES +
                "(" +
                KEY_CURVE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + // Define a primary key
                KEY_CURVE_N + " INTEGER" + "," +
                KEY_CURVE_X + " REAL" + "," +
                KEY_CURVE_Y + " REAL" + "," +
                KEY_CURVE_ROTATION + " INTEGER" + "," +
                KEY_CURVE_LINE_LENGTH + " INTEGER" + "," +
                KEY_CURVE_WIDTH + " INTEGER" + "," +
                KEY_CURVE_COLOR + " VARCHAR(10)" +
                ")";

        db.execSQL(CREATE_CURVES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURVES);
            onCreate(db);
        }
    }

    public void addCurve(Curve curve) {
        SQLiteDatabase db = getWritableDatabase();
        String TAG = "ADD";

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_CURVE_N, curve.curveN);
            values.put(KEY_CURVE_X, curve.curveX);
            values.put(KEY_CURVE_Y, curve.curveY);
            values.put(KEY_CURVE_ROTATION, curve.curveRotation);
            values.put(KEY_CURVE_LINE_LENGTH, curve.curveLineLength);
            values.put(KEY_CURVE_WIDTH, curve.curveWidth);
            values.put(KEY_CURVE_COLOR, curve.curveColor);

            db.insertOrThrow(TABLE_CURVES, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add curve to database");
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Curve> getAllCuves() {
        ArrayList<Curve> curves = new ArrayList<>();
        String TAG = "GET";

        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s ",
                        TABLE_CURVES);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Curve newCurve = new Curve();
                    newCurve.curveId = cursor.getInt(cursor.getColumnIndex(KEY_CURVE_ID));
                    newCurve.curveN = cursor.getInt(cursor.getColumnIndex(KEY_CURVE_N));
                    newCurve.curveX = cursor.getDouble(cursor.getColumnIndex(KEY_CURVE_X));
                    newCurve.curveY = cursor.getDouble(cursor.getColumnIndex(KEY_CURVE_Y));
                    newCurve.curveRotation = cursor.getInt(cursor.getColumnIndex(KEY_CURVE_ROTATION));
                    newCurve.curveLineLength = cursor.getInt(cursor.getColumnIndex(KEY_CURVE_LINE_LENGTH));
                    newCurve.curveWidth = cursor.getInt(cursor.getColumnIndex(KEY_CURVE_WIDTH));
                    newCurve.curveColor = cursor.getString(cursor.getColumnIndex(KEY_CURVE_COLOR));

                    curves.add(newCurve);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get curves from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return curves;
    }

    public Curve getCurveById(int curve_id) {
        ArrayList<Curve> curves = new ArrayList<>();
        String TAG = "GET";
        Curve curve = new Curve();

        String CURVE_SELECT_QUERY =
                String.format("SELECT * FROM %s WHERE %s=%s",
                        TABLE_CURVES, KEY_CURVE_ID, curve_id);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(CURVE_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    curve.curveId = cursor.getInt(cursor.getColumnIndex(KEY_CURVE_ID));
                    curve.curveN = cursor.getInt(cursor.getColumnIndex(KEY_CURVE_N));
                    curve.curveX = cursor.getDouble(cursor.getColumnIndex(KEY_CURVE_X));
                    curve.curveY = cursor.getDouble(cursor.getColumnIndex(KEY_CURVE_Y));
                    curve.curveRotation = cursor.getInt(cursor.getColumnIndex(KEY_CURVE_ROTATION));
                    curve.curveLineLength = cursor.getInt(cursor.getColumnIndex(KEY_CURVE_LINE_LENGTH));
                    curve.curveWidth = cursor.getInt(cursor.getColumnIndex(KEY_CURVE_WIDTH));
                    curve.curveColor = cursor.getString(cursor.getColumnIndex(KEY_CURVE_COLOR));
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get curves from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return curve;
    }

    public void clearCurves() {
        SQLiteDatabase db = getWritableDatabase();
        String TAG = "CLEAR";

        db.delete(TABLE_CURVES,null,null);
        db.close();
    }

    public void deleteCurve(int curve_id) {
        SQLiteDatabase db = getWritableDatabase();
        String TAG = "DELETE";
        db.delete(TABLE_CURVES, KEY_CURVE_ID + " = ?", new String[] {(Integer.toString(curve_id))});
        db.close();
    }
}