package com.github.hondams.appbase.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import com.github.hondams.appbase.model.CodeEnum;
import com.github.hondams.appbase.model.CodeEnumUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 以下で設定する。
// org.mybatis.spring.SqlSessionFactoryBean.setDefaultEnumTypeHandler(java.lang.Class<? extends
// org.apache.ibatis.type.TypeHandler>)
// org.apache.ibatis.type.EnumTypeHandler<Enum<E>>のように、Enumクラスを受け取るコンストラクタがあればよい。
@RequiredArgsConstructor
public class CodeEnumTypeHandler<T extends CodeEnum> extends BaseTypeHandler<T> {

    @Getter
    private final Class<T> codeEnumType;

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, parameter.getCode());
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return CodeEnumUtils.getCodeEnum(this.codeEnumType, rs.getString(columnName));
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return CodeEnumUtils.getCodeEnum(this.codeEnumType, rs.getString(columnIndex));
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return CodeEnumUtils.getCodeEnum(this.codeEnumType, cs.getString(columnIndex));
    }

}
