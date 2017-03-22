package com.example.whetherdata.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.whetherdata.model.Weather;

import java.util.ArrayList;
import java.util.List;


public class WeatherDataHandler extends SQLiteOpenHelper {

	// All static variables
	private static final int DATABASE_VERSION = 3;
	
	public static final String DATABASE_NAME = "WEATHER";
	
	public static final String DB_PATH = "/data/data/com.example.whetherdata/databases/";
	/*
	private int _id;
    private String weatherType;
    private String name;
    private String humidity;
    private String pressure;
    private String temperature;
    private Long time;
	*/
	
	public static final String WEATHER_TABLE_NAME = "Weather";

	public static final String KEY_WEATHER_ID = "_id";
	public static final String KEY_WEATHER_WEATHER_TYPE= "weatherType";
	public static final String KEY_WEATHER_NAME = "name";
	public static final String KEY_WEATHER_HUMIDITY = "humidity";
	public static final String KEY_WEATHER_PRESSURE = "pressure";
	public static final String KEY_WEATHER_TEMPERATURE = "temperature";
	public static final String KEY_WEATHER_TIME = "timestamp";

	private static final String WEATHER_TABLE_CREATE = "CREATE TABLE "
			+ WEATHER_TABLE_NAME + "("
			+ KEY_WEATHER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ KEY_WEATHER_WEATHER_TYPE + " TEXT, "
			+ KEY_WEATHER_NAME + " TEXT, "
			+ KEY_WEATHER_HUMIDITY + " TEXT, "
			+ KEY_WEATHER_PRESSURE + " TEXT, "
			+ KEY_WEATHER_TEMPERATURE + " TEXT, "
			+ KEY_WEATHER_TIME + " LONG "
			+");";

	
	public static final String[] WEATHER_COLUMNS = new String[] {
			KEY_WEATHER_ID,
			KEY_WEATHER_WEATHER_TYPE,
			KEY_WEATHER_NAME,
			KEY_WEATHER_HUMIDITY,
			KEY_WEATHER_PRESSURE,
			KEY_WEATHER_TEMPERATURE,
			KEY_WEATHER_TIME

		};	
		
	private static final String TAG = WeatherDataHandler.class.getSimpleName() ;

	private Context context;

	
	public WeatherDataHandler(Context context) {

		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(WEATHER_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + WEATHER_TABLE_NAME);
		onCreate(db);

	}


	/**
	 * Add Weather to database
	 * (Note: Id variable in the weather will not be inserted.)
	 *
	 * @param weather Weather to insert
	 */
	public void add(Weather weather) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_WEATHER_WEATHER_TYPE,weather.getWeatherType());
		values.put(KEY_WEATHER_NAME,weather.getName());
		values.put(KEY_WEATHER_HUMIDITY, weather.getHumidity());
		values.put(KEY_WEATHER_PRESSURE, weather.getPressure());
		values.put(KEY_WEATHER_TEMPERATURE,weather.getTemperature());
		values.put(KEY_WEATHER_TIME,weather.getTime());


		db.insertWithOnConflict(WEATHER_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		db.close();
	}

	/**
	 * Update Weather to database
	 * (Note: Id variable in the weather will not be inserted.)
	 *
	 * @param weather Weather to update
	 */
	public int update(Weather weather) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_WEATHER_WEATHER_TYPE,weather.getWeatherType());
		values.put(KEY_WEATHER_NAME,weather.getName());
		values.put(KEY_WEATHER_HUMIDITY, weather.getHumidity());
		values.put(KEY_WEATHER_PRESSURE, weather.getPressure());
		values.put(KEY_WEATHER_TEMPERATURE,weather.getTemperature());
		values.put(KEY_WEATHER_TIME,weather.getTime());

		int result = db.update(WEATHER_TABLE_NAME,values,KEY_WEATHER_ID + " = " + weather.getId(),null);
		db.close();
		return result;
	}
	
	/**
	 * Add List of Weathers
	 * 
	 * @param weatherList  List<Weather> to be inserted
	 */
	public void add(List<Weather> weatherList){
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransaction();
		try{
				for(int i = 0;i<weatherList.size();i++){
					
						Weather weather = weatherList.get(i);

						ContentValues values= new ContentValues();
						values.put(KEY_WEATHER_WEATHER_TYPE,weather.getWeatherType());
						values.put(KEY_WEATHER_NAME,weather.getName());
						values.put(KEY_WEATHER_HUMIDITY, weather.getHumidity());
						values.put(KEY_WEATHER_PRESSURE, weather.getPressure());
						values.put(KEY_WEATHER_TEMPERATURE,weather.getTemperature());
						values.put(KEY_WEATHER_TIME,weather.getTime());
						db.insertWithOnConflict(WEATHER_TABLE_NAME,null,values,SQLiteDatabase.CONFLICT_REPLACE);
				}
				db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
		db.close();
	}

	/**
	 * Fetch weather from database using id of weather
	 * 
	 * @param weatherid
	 * @return
	 */
	public Weather getWeather(int weatherid) {

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(WEATHER_TABLE_NAME, WEATHER_COLUMNS, KEY_WEATHER_ID + "=?",
				new String[] { String.valueOf(weatherid) }, null, null, null);
		if (cursor == null || cursor.getCount() < 1) {

		} else {
			cursor.moveToFirst();
			int _id;
			String weatherType;
			String name;
			String humidity;
			String pressure;
			String temperature;
			Long time;
			
			_id = cursor.getInt(0);
			weatherType = cursor.getString(1);
			name = cursor.getString(2);
			humidity = cursor.getString(3);
			pressure = cursor.getString(4);
			temperature = cursor.getString(5);
			time = cursor.getLong(6);

			Weather w;
			try {
				w = new Weather(_id,weatherType,name,humidity,pressure,temperature,time);
				db.close();
				return w;
			} catch (Exception e) {

			}
			
			db.close();
			return null;
		}
		db.close();
		return null;
	}
		
	
	/**
	 * Fetch All weathers from the Database
	 * @return
	 */
	public ArrayList<Weather> getWeathers() {
		ArrayList<Weather> weatherList = new ArrayList<Weather>();

		String selectQuery = "SELECT * FROM " + WEATHER_TABLE_NAME + " ORDER BY LOWER (" + KEY_WEATHER_ID+ ") DESC";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		Weather w;
		if (cursor.moveToFirst()) {
			do {
				int _id;
				String weatherType;
				String name;
				String humidity;
				String pressure;
				String temperature;
				Long time;

				_id = cursor.getInt(0);
				weatherType = cursor.getString(1);
				name = cursor.getString(2);
				humidity = cursor.getString(3);
				pressure = cursor.getString(4);
				temperature = cursor.getString(5);
				time = cursor.getLong(6);

				try {
					w = new Weather(_id,weatherType,name,humidity,pressure,temperature,time);
					weatherList.add(w);
					db.close();

				} catch (Exception e) {

				}
				
			} while (cursor.moveToNext());
		}

		db.close();
		return weatherList;
	}




	public Cursor getWeatherCursor(){
		String selectQuery = "SELECT * FROM " + WEATHER_TABLE_NAME + " ORDER BY LOWER (" + KEY_WEATHER_ID+ ")";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		return cursor;
	}
		
	
	/**
	 * Get the number of weathers in database
	 * @return
	 */
	public int getWeatherCount() {
		String countQuery = "SELECT * FROM " + WEATHER_TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		return count;
	}


}
