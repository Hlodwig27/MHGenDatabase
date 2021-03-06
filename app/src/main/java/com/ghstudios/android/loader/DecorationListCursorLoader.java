package com.ghstudios.android.loader;

import android.content.Context;
import android.database.Cursor;

import com.ghstudios.android.data.DataManager;

public class DecorationListCursorLoader extends SQLiteCursorLoader {

	private String filter;
	public DecorationListCursorLoader(Context context, String filter) {
		super(context);
		this.filter = filter;
	}

	@Override
	protected Cursor loadCursor() {
		// Query the list of decorations. Null/empty strings are handled
		return DataManager.get().queryDecorationsSearch(filter);
	}
}
