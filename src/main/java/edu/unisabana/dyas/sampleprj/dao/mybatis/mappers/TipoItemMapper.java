package edu.unisabana.dyas.sampleprj.dao.mybatis.mappers;


import java.util.List;
import org.apache.ibatis.annotations.Param;

import edu.unisabana.dyas.samples.entities.TipoItem;

public interface TipoItemMapper {
    
    
    public List<TipoItem> getTiposItems();

    public TipoItem getTipoItem(@Param("id") int id);

    public void addTipoItem(@Param("descripcion") String des);

}
