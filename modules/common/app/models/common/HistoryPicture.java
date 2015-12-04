package models.common;

import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * Created by ypzhuang on 15/12/2.
 */

@Entity
@Table(name = "t_pic_file_history")
public class HistoryPicture extends Model {
    @Id
    public Long id;

    @Column(name="creattime")
    public Date createTime;

    @Column(name="storagepath")
    public String storagePath;

    @Column(name="partnum")
    public Integer partNum;
    public Long lid;

    @Column(name="ltr_id")
    public Long ltrId;


    @Column(name="size_info")
    public String sizeInfo;

    @Column(name="thumb_path")
    public String thumbPath;


    public Integer rotation; //旋转角度，值范围（0－360）的整数，这个值代表服务端需要顺时针旋转的角度；

    public Integer rotated; //是否已调整，1 代表已经调整；0或者null且 rotation > 0,意味着需要旋转

    public Date createDate = new Date();

    public static Finder<Long, HistoryPicture> find = new Finder<>(Long.class, HistoryPicture.class);

    public static HistoryPicture findPictureByLtrId(Long ltrId) {
        List<HistoryPicture> pictures = HistoryPicture.find.where()
                .eq("ltrId", ltrId).findList();
        if (!pictures.isEmpty()) {
            return pictures.get(0);
        }
        return null;
    }
}
