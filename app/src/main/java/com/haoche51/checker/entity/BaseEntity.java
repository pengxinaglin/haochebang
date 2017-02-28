package com.haoche51.checker.entity;

import com.google.gson.Gson;

public abstract class BaseEntity {
	protected static Gson gson = null;
	
	static {
		gson = new Gson();
	}
}
