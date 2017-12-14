package fsy.api.dao;/**
 * Created by zln on 2017/12/1.
 */

import com.alibaba.fastjson.JSONObject;
import fsy.utils.JsonRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;

/**
 * 描述:
 * 数据访问通用类
 *
 * @auth zln
 * @create 2017-12-01 14:17
 */

public class BaseDao {

    private  JdbcTemplate jdbcTemplate;

    public  static final JsonRowMapper JSON_ROW_MAPPER = new JsonRowMapper();

    @Autowired
    public void initJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<JSONObject> queryForJsonList(String sql, Object... args) {
        return jdbcTemplate.query(sql, JSON_ROW_MAPPER, args);
    }

    /**
     * <B>方法名称：</B>查询JSON数据<BR>
     * <B>概要说明：</B><BR>
     *
     * @param sql SQL语句
     * @param args 参数
     * @return JSONObject JSON数据
     */
    public JSONObject queryForJsonObject(String sql, Object... args) {
        List<JSONObject> jsonList = queryForJsonList(sql, args);
        if (jsonList == null || jsonList.size() < 1) {
            return null;
        }
        return jsonList.get(0);
    }

    /**
     * <B>方法名称：</B>单表INSERT方法<BR>
     * <B>概要说明：</B>单表INSERT方法<BR>
     * @param tableName 表名
     * @param data JSONObject对象
     */
    protected int insert(String tableName, JSONObject data) {

        if (data.size() <= 0) {
            return 0;
        }

        StringBuffer sql = new StringBuffer();
        sql.append(" INSERT INTO ");
        sql.append(tableName + " ( ");

        Set<Entry<String, Object>> set = data.entrySet();
        List<Object> sqlArgs = new ArrayList<Object>();
        for (Iterator<Entry<String, Object>> iterator = set.iterator(); iterator.hasNext();) {
            Entry<String, Object> entry = (Entry<String, Object>) iterator.next();
            sql.append(entry.getKey() + ",");
            sqlArgs.add(entry.getValue());
        }

        sql.delete(sql.length() - 1, sql.length());
        sql.append(" ) VALUES ( ");
        for (int i = 0; i < set.size(); i++) {
            sql.append("?,");
        }

        sql.delete(sql.length() - 1, sql.length());
        sql.append(" ) ");

        return jdbcTemplate.update(sql.toString(), sqlArgs.toArray());
    }


    /**
     * <B>方法名称：</B>批量新增數據方法<BR>
     * <B>概要说明：</B><BR>
     * @param tableName 数据库表名称
     * @param list 插入数据集合
     */
    protected void insertBatch(String tableName, final List<LinkedHashMap<String, Object>> list) {

        if (list.size() <= 0) {
            return;
        }

        LinkedHashMap<String, Object> linkedHashMap = list.get(0);

        StringBuffer sql = new StringBuffer();
        sql.append(" INSERT INTO ");
        sql.append(tableName + " ( ");

        final String[] keyset =  (String[]) linkedHashMap.keySet().toArray(new String[linkedHashMap.size()]);

        for (int i = 0; i < linkedHashMap.size(); i++) {
            sql.append(keyset[i] + ",");
        }

        sql.delete(sql.length() - 1, sql.length());

        sql.append(" ) VALUES ( ");
        for (int i = 0; i < linkedHashMap.size(); i++) {
            sql.append("?,");
        }

        sql.delete(sql.length() - 1, sql.length());
        sql.append(" ) ");

        jdbcTemplate.batchUpdate(sql.toString(), new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                LinkedHashMap<String, Object> map = list.get(i);
                Object[] valueset = map.values().toArray(new Object[map.size()]);
                int len = map.keySet().size();
                for (int j = 0; j < len; j++) {
                    ps.setObject(j + 1, valueset[j]);
                }
            }
            public int getBatchSize() {
                return list.size();
            }
        });
    }

    protected int update(String sql) {
        return jdbcTemplate.update(sql);
    }

}

