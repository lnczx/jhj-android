package com.meijialife.dingdang.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.FinalDb.DbUpdateListener;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.meijialife.dingdang.bean.User;
import com.meijialife.dingdang.bean.UserInfo;

/**
 * 数据库
 *
 *
 */
public class DBHelper {

	private Context context;
	private FinalDb finalDb;
	private static DBHelper helper = null;
	private String DBNAME = "simi.db";
	private int DBVERSION = 1;

	synchronized public static DBHelper getInstance(Context context) {
		if (helper == null) {
			helper = new DBHelper(context);
		}
		return helper;
	}

	public DBHelper(Context context) {
		this.context = context;
		finalDb = FinalDb.create(context, DBNAME, false, DBVERSION,
				new DbUpdateListener() {

					public void onUpgrade(SQLiteDatabase db, int oldVersion,
							int newVersion) {

					}
				});

	}

	/**
	 * 添加
	 *
	 * @param entity
	 */
	public <T> void add(T t, Object id) {
		Object obj = searchById(t.getClass(), id);
		if (obj != null) { // 数据库中存在记录 更新
			finalDb.update(t);
		} else { // 数据库中没有记录，插入新记录
			finalDb.save(t);
		}
	}

	/**
	 * 删除所有记录
	 *
	 * @param clazz
	 */
	public <T> void deleteAll(Class<T> clazz) {
		finalDb.deleteByWhere(clazz, null);// 清空用户表
	}

	/**
	 * 删除记录根据id删除
	 *
	 * @param clazz
	 */
	public <T> void deleteAll(T t) {
		finalDb.delete(t);
	}

	/**
	 * 根据id删除记录
	 *
	 * @param clazz
	 * @param id
	 */
	public <T> void deleteById(T t, Object id) {
		finalDb.deleteById(t.getClass(), id);
	}

	/**
	 * 根据条件删除记录
	 *
	 * @param clazz
	 * @param strWhere
	 *            条件
	 */
	public <T> void deleteByWhere(Class<T> clazz, String strWhere) {
		finalDb.deleteByWhere(clazz, strWhere);
	}

	/**
	 * 根据id 更新
	 *
	 * @param t
	 */
	public <T> void update(T t, Object id) {
		Object obj = searchById(t.getClass(), id);
		if (obj != null) { // 数据库中存在记录 更新
			finalDb.update(t);
		} else { // 数据库中没有记录，插入新记录
			finalDb.save(t);
		}
	}

	/**
	 * 根据wehere条件更新
	 *
	 * @param t
	 * @param strWhere
	 */
	public <T> void updateByWhere(T t, String strWhere) {
		finalDb.update(t, strWhere);
	}

	/**
	 * 查询全部
	 *
	 * @param clazz
	 * @return
	 */
	public <T> List<T> searchAll(Class<T> clazz) {
		List<T> list = finalDb.findAll(clazz);
		return list;
	}

	/**
	 * 根据id查询
	 *
	 * @param clazz
	 * @param id
	 * @return
	 */
	public <T> T searchById(Class<T> clazz, Object id) {
		return finalDb.findById(id, clazz);
	}

	/**
	 * 根据id查询
	 *
	 * @param clazz
	 * @param id
	 * @return
	 */
	public <T> T searchById(T t, Object id) {
		return (T) finalDb.findById(id, t.getClass());
	}

	/**
	 * 根据where条件查询全部
	 *
	 * @param clazz
	 * @param id
	 * @return
	 */
	public <T> List<T> searchAllByWhere(Class<T> clazz, String strWhere) {
		return finalDb.findAllByWhere(clazz, strWhere);
	}

	/**
	 * 添页面的缓存(清空表重新插新数据)
	 *
	 * @param <T>
	 * @param list
	 */
	public <T> void addList2Db(List<T> list) {
		if (list != null && list.size() > 0) {
			// 清空数据库
			finalDb.deleteByWhere(list.get(0).getClass(), null);
			// 添加数据
			for (int i = 0; i < list.size(); i++) {
				finalDb.save(list.get(i));
			}

		}

	}

	/**
	 * 获取页面缓存
	 *
	 * @param clazz
	 *            bean
	 * @return list<T>
	 */
	public <T> List<T> getList4Db(Class<T> clazz) {
		List<T> list = null;
		try {
			list = finalDb.findAll(clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 获取本地基础数据
	 */
//	public BaseData getBase() {
//		List<BaseData> searchList = searchAll(BaseData.class);
//		if (searchList != null && searchList.size() > 0) {
//			return searchList.get(0);
//		}
//		return null;
//	}

	/**
	 * 根据城市id获取小区列表
	 *
	 * @param clazz
	 *            bean
	 * @return list<T>
	 */
	public <T> List<T> getCellsList4Db(Class<T> clazz, String cityId) {
		List<T> list = null;
		try {
			String sqlWhere = "C_CITY_ID = " + cityId
					+ " and C_IN_CONTROLL = 1";
			list = finalDb.findAllByWhere(clazz, sqlWhere);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 根据某个字段查询一条数据
	 *
	 * @param clazz
	 *            数据类
	 * @param column
	 *            字段名
	 * @param value
	 *            字段值
	 *
	 * @return 数据实体
	 */
	public <T> Serializable getDataForValue(Class<T> clazz, String column,
			String value) {
		ArrayList<Class<T>> list = new ArrayList<Class<T>>();
		Serializable data;
		String sqlWhere = column + "=" + "'" + value + "'";
		list = (ArrayList<Class<T>>) searchAllByWhere(clazz, sqlWhere);
		if (list.size() > 0) {
			data = list.get(0);
			return data;
		} else {
			return null;
		}
	}

	/**
	 * 根据小区名称获取小区数据
	 *
	 * @param c_name
	 * @return
	 */
	/*
	 * public CellsData getCellsData(String c_name){ ArrayList<CellsData> list =
	 * new ArrayList<CellsData>(); CellsData cellData; String sqlWhere =
	 * "C_NAME="+"'" + c_name + "'"; list = (ArrayList<CellsData>)
	 * searchAllByWhere(CellsData.class, sqlWhere); if(list.size() > 0){
	 * cellData = list.get(0); return cellData; }else{ return null; } }
	 */

	/**
	 * 更新当前登录的用户数据
	 * @return
	 */
	public static void updateUser(Context context, User user){
	    getInstance(context).deleteAll(User.class);
	    getInstance(context).add(user, user.getId());
	}
	
	/**
	 * 更新当前用户详情数据
	 * @return
	 */
	public static void updateUserInfo(Context context, UserInfo userInfo){
	    getInstance(context).deleteAll(UserInfo.class);
	    getInstance(context).add(userInfo, userInfo.getId());
	}
	
	/**
	 * 获取当前登陆的用户
	 * @return
	 */
	public static User getUser(Context context){
	    List<User> list = getInstance(context).searchAll(User.class);
	    if(list.size() > 0){
	        return list.get(0);
	    }
	    return null;
	}
	
	/**
	 * 获取当前用户的详情
	 * @return
	 */
	public static UserInfo getUserInfo(Context context){
	    List<UserInfo> list = getInstance(context).searchAll(UserInfo.class);
	    if(list.size() > 0){
	        return list.get(0);
	    }
	    return null;
	}
	
	 
	
	 
    
    
    

}
