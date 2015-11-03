package models.common;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.SqlRow;
import play.Logger;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ypzhuang on 15/10/15.
 */

@Entity
@Table(name = "t_action")
public class Action extends Model {
    @Id
    public Long id;

    @Column(nullable = false)
    public String module;

    @Column(nullable = false)
    public String uri;

    @Column(nullable = false)
    public String mappingAction;

    @Column(nullable = false)
    public String method;


    public static Finder<Long, Action> find = new Finder<>(Long.class, Action.class);

    public static List<Action> findAll(){
        return Action.find.findList();
    }


    public static List<String> findAllModules(){

        List<String> modules = new ArrayList<String>();
        String sql = "select module from t_action group by module";


        List<SqlRow> sqlRows = Ebean.createSqlQuery(sql).findList();
        for(SqlRow sqlRow :sqlRows) {
            modules.add(sqlRow.getString("module"));
        }

        return modules;
    }

    public static List<String> findAllUrisByModule(String module){
        List<String> uris = new ArrayList<String>();
        String sql = "select uri from t_action where module = :module  group by uri order by uri";
        List<SqlRow> sqlRows = Ebean.createSqlQuery(sql)
                .setParameter("module", module)
                .findList();
        for(SqlRow sqlRow :sqlRows) {
            uris.add(sqlRow.getString("uri"));
        }

        return uris;

    }


    public static List<String> findAllMethodByModuleAndUri(String module,String uri){
        List<String> methods = new ArrayList<String>();

        List<Action> actions  = Action.find.where()
                .eq("module", module)
                .eq("uri", uri)
                .findList();

        for (Action action :actions) {
            methods.add(action.method);
        }
        return methods;
    }


    public static List<Action> findAllByModuleAndUriAndMethod(String module,String uri, String method) {

        List<Action> actions  = Action.find.where()
                .eq("module", module)
                .eq("uri", uri)
                .eq("method", method)
                .findList();

        return actions;
    }


}
