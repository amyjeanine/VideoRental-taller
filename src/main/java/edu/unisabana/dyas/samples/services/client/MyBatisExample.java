/*
 * Copyright (C) 2015 cesarvefe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.unisabana.dyas.samples.services.client;



import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import edu.unisabana.dyas.sampleprj.dao.mybatis.mappers.ClienteMapper;
import edu.unisabana.dyas.sampleprj.dao.mybatis.mappers.ItemMapper;
import edu.unisabana.dyas.sampleprj.dao.mybatis.mappers.TipoItemMapper;
import edu.unisabana.dyas.samples.entities.Cliente;
import edu.unisabana.dyas.samples.entities.Item;
import edu.unisabana.dyas.samples.entities.TipoItem;

/**
 *
 * @author cesarvefe
 */
public class MyBatisExample {

    /**
     * Método que construye una fábrica de sesiones de MyBatis a partir del
     * archivo de configuración ubicado en src/main/resources
     *
     * @return instancia de SQLSessionFactory
     */
    public static SqlSessionFactory getSqlSessionFactory() {
        SqlSessionFactory sqlSessionFactory = null;
        if (sqlSessionFactory == null) {
            InputStream inputStream;
            try {
                inputStream = Resources.getResourceAsStream("mybatis-config.xml");
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e.getCause());
            }
        }
        return sqlSessionFactory;
    }

    /**
     * Programa principal de ejempo de uso de MyBATIS
     * @param args
     * @throws SQLException 
     */
    public static void main(String args[]) throws SQLException {
        SqlSessionFactory sessionfact = getSqlSessionFactory();

        SqlSession sqlss = sessionfact.openSession();

        try {
            ClienteMapper cm = sqlss.getMapper(ClienteMapper.class);
            ItemMapper im = sqlss.getMapper(ItemMapper.class);
            TipoItemMapper tm = sqlss.getMapper(TipoItemMapper.class);

            System.out.println("== consultarClientes() ==");
            System.out.println(cm.consultarClientes());

            System.out.println("\n== consultarCliente(123456789) ==");
            Cliente cliente = cm.consultarCliente(123456789);
            System.out.println(cliente);

            System.out.println("\n== consultarItems() ==");
            System.out.println(im.consultarItems());

            System.out.println("\n== consultarItem(1) ==");
            Item item = im.consultarItem(1);
            System.out.println(item);

            System.out.println("\n== getTiposItems() ==");
            System.out.println(tm.getTiposItems());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            cm.agregarItemRentadoACliente(123456789, 2,
                    new java.sql.Date(sdf.parse("2024-04-01").getTime()),
                    new java.sql.Date(sdf.parse("2024-04-05").getTime()));

            Item nuevo = new Item();
            nuevo.setId(4);
            nuevo.setNombre("Taladro");
            nuevo.setDescrpcion("Taladro inalambrico");
            nuevo.setFechaLanzamiento(sdf.parse("2024-01-10"));
            nuevo.setTarifaxDia(3000);
            nuevo.setFormatoRenta("Diario");
            nuevo.setGenero("Herramienta");
            TipoItem tipoHerramienta = new TipoItem(3, "Herramienta");
            nuevo.setTipo(tipoHerramienta);
            im.insertarItem(nuevo);

            sqlss.commit();

            System.out.println("\n== cliente luego de agregar item rentado ==");
            System.out.println(cm.consultarCliente(123456789));

            System.out.println("\n== items luego de insertar uno nuevo ==");
            System.out.println(im.consultarItems());

        } catch (Exception e) {
            sqlss.rollback();
            throw new RuntimeException(e);
        } finally {
            sqlss.close();
        }
    }


}
