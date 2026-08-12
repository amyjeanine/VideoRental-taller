package edu.unisabana.dyas.sampleprj.dao.mybatis.typehandlers;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * Igual que SqlDateAsTextTypeHandler, pero para java.util.Date (Item.fechaLanzamiento).
 */
public class UtilDateAsTextTypeHandler extends BaseTypeHandler<Date> {

    private static final String PATTERN = "yyyy-MM-dd";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Date parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, new SimpleDateFormat(PATTERN).format(parameter));
    }

    @Override
    public Date getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parse(rs.getString(columnName));
    }

    @Override
    public Date getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parse(rs.getString(columnIndex));
    }

    @Override
    public Date getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parse(cs.getString(columnIndex));
    }

    private Date parse(String value) throws SQLException {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return new SimpleDateFormat(PATTERN).parse(value);
        } catch (ParseException e) {
            throw new SQLException("No se pudo parsear la fecha '" + value + "' con el formato " + PATTERN, e);
        }
    }
}
