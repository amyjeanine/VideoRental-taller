package edu.unisabana.dyas.sampleprj.dao.mybatis.typehandlers;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * ResultSet.getDate() de sqlite-jdbc truena con fechas guardadas como texto
 * plano "yyyy-MM-dd" (sin hora). Este handler lee la columna como String y la
 * parsea a mano para evitarlo.
 */
public class SqlDateAsTextTypeHandler extends BaseTypeHandler<java.sql.Date> {

    private static final String PATTERN = "yyyy-MM-dd";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, java.sql.Date parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, new SimpleDateFormat(PATTERN).format(parameter));
    }

    @Override
    public java.sql.Date getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parse(rs.getString(columnName));
    }

    @Override
    public java.sql.Date getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parse(rs.getString(columnIndex));
    }

    @Override
    public java.sql.Date getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parse(cs.getString(columnIndex));
    }

    private java.sql.Date parse(String value) throws SQLException {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return new java.sql.Date(new SimpleDateFormat(PATTERN).parse(value).getTime());
        } catch (ParseException e) {
            throw new SQLException("No se pudo parsear la fecha '" + value + "' con el formato " + PATTERN, e);
        }
    }
}
