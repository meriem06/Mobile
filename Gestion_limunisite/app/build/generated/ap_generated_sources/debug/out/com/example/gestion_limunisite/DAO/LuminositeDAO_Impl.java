package com.example.gestion_limunisite.DAO;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.gestion_limunisite.entity.Converter;
import com.example.gestion_limunisite.entity.Luminosite;
import java.lang.Class;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class LuminositeDAO_Impl implements LuminositeDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Luminosite> __insertionAdapterOfLuminosite;

  private final EntityDeletionOrUpdateAdapter<Luminosite> __deletionAdapterOfLuminosite;

  private final EntityDeletionOrUpdateAdapter<Luminosite> __updateAdapterOfLuminosite;

  public LuminositeDAO_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLuminosite = new EntityInsertionAdapter<Luminosite>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `Luminosite` (`id`,`intensite`,`isNormal`,`date`,`userId`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final Luminosite entity) {
        statement.bindLong(1, entity.getId());
        statement.bindDouble(2, entity.getIntensite());
        final int _tmp = entity.isNormal() ? 1 : 0;
        statement.bindLong(3, _tmp);
        final Long _tmp_1 = Converter.dateToTimestamp(entity.getDate());
        if (_tmp_1 == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, _tmp_1);
        }
        statement.bindLong(5, entity.getUserId());
      }
    };
    this.__deletionAdapterOfLuminosite = new EntityDeletionOrUpdateAdapter<Luminosite>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `Luminosite` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final Luminosite entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfLuminosite = new EntityDeletionOrUpdateAdapter<Luminosite>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `Luminosite` SET `id` = ?,`intensite` = ?,`isNormal` = ?,`date` = ?,`userId` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final Luminosite entity) {
        statement.bindLong(1, entity.getId());
        statement.bindDouble(2, entity.getIntensite());
        final int _tmp = entity.isNormal() ? 1 : 0;
        statement.bindLong(3, _tmp);
        final Long _tmp_1 = Converter.dateToTimestamp(entity.getDate());
        if (_tmp_1 == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, _tmp_1);
        }
        statement.bindLong(5, entity.getUserId());
        statement.bindLong(6, entity.getId());
      }
    };
  }

  @Override
  public long insert(final Luminosite luminosite) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      final long _result = __insertionAdapterOfLuminosite.insertAndReturnId(luminosite);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteLuminosite(final Luminosite luminosite) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfLuminosite.handle(luminosite);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateLuminosite(final Luminosite luminosite) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfLuminosite.handle(luminosite);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Luminosite> getAll() {
    final String _sql = "SELECT * FROM luminosite";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfIntensite = CursorUtil.getColumnIndexOrThrow(_cursor, "intensite");
      final int _cursorIndexOfIsNormal = CursorUtil.getColumnIndexOrThrow(_cursor, "isNormal");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final List<Luminosite> _result = new ArrayList<Luminosite>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final Luminosite _item;
        _item = new Luminosite();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final float _tmpIntensite;
        _tmpIntensite = _cursor.getFloat(_cursorIndexOfIntensite);
        _item.setIntensite(_tmpIntensite);
        final boolean _tmpIsNormal;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsNormal);
        _tmpIsNormal = _tmp != 0;
        _item.setNormal(_tmpIsNormal);
        final Date _tmpDate;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfDate);
        }
        _tmpDate = Converter.fromTimestamp(_tmp_1);
        _item.setDate(_tmpDate);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _item.setUserId(_tmpUserId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Luminosite getLuminositeById(final long id) {
    final String _sql = "SELECT * FROM luminosite WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfIntensite = CursorUtil.getColumnIndexOrThrow(_cursor, "intensite");
      final int _cursorIndexOfIsNormal = CursorUtil.getColumnIndexOrThrow(_cursor, "isNormal");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final Luminosite _result;
      if (_cursor.moveToFirst()) {
        _result = new Luminosite();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final float _tmpIntensite;
        _tmpIntensite = _cursor.getFloat(_cursorIndexOfIntensite);
        _result.setIntensite(_tmpIntensite);
        final boolean _tmpIsNormal;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfIsNormal);
        _tmpIsNormal = _tmp != 0;
        _result.setNormal(_tmpIsNormal);
        final Date _tmpDate;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfDate);
        }
        _tmpDate = Converter.fromTimestamp(_tmp_1);
        _result.setDate(_tmpDate);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        _result.setUserId(_tmpUserId);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
