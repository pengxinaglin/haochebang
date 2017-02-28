package com.haoche51.checker.entity;

import android.net.Uri;

public class Pictures {
	public static final String ID = "_id";
	public static final String PATH = "_path";
	public static final String COMMENT = "_comment";
	public static final String URL = "_url";
	public static final String POSITION_X = "_position_x";
	public static final String POSITION_Y = "_position_y";
	public static final String DEFAULT_SORT_ORDER = "_id aes";
	public static final String AUTHORITY = "com.haoche51.checker.entity.pictures";
	public static final String TYPE = "_type";
	public static final int ITEM = 1;
	public static final int ITEM_ID = 2;
	public static final int ITEM_POS = 3;  
	
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.haoche51.checker.entity.picture";  
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.haoche51.checker.entity.picture";  
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/item");
	public static final Uri CONTENT_POS_URI = Uri.parse("content://" + AUTHORITY + "/pos");
	
	
}
