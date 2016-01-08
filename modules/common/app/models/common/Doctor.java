package models.common;

import com.avaje.ebean.Expr;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import play.Logger;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

/**
 * Created by ypzhuang on 15/9/23.
 */
@Entity
@Table(name = "t_doctor")
public class Doctor  extends Model {
    @Id
    public Long id;

    private String doctorName;

    private String nickname;//真名

    private String img;//头像

    private String licenseImg;//执照

    private String qrImg;//QR

    private String detail;//个人信息

    private Integer gender;

    private Integer age;

    private String password;

    private String email;

    private String tel;//手机

    private String gid;//通讯编号

    private Integer deleted;

    private Date createTime;//创建时间 add by harper 2015-1-12

    private Date lastLoginTime;//登录时间 add by harper 2015-1-12

    private Long hid;//hospital

    private String hospitalName;//医院名称

    private Long did;//depaermentId

    private String department;//子科室name

    private Long tid;//titleId

    private String title;//职称

    private String departmentTel;//科室电话

    private Integer active;//激活0未激活 1激活

    private Integer clientType;//注册机器type

    private String introduction; //个人简介,签名

    public Long getDid() {
        return did;
    }

    public Long getTid() {
        return tid;
    }

    public String getTitle() {
        return title;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name="licenseImg")
    public String getLicenseImg() {
        return licenseImg;
    }

    public void setLicenseImg(String licenseImg) {
        this.licenseImg = licenseImg;
    }

    @Column(name="clientType")
    public Integer getClientType() {
        return clientType;
    }

    public void setClientType(Integer clientType) {
        this.clientType = clientType;
    }

    @Column(name="lastLoginTime")
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @Column(name="doctor_name")
    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    @JsonIgnore
    @Column(name="password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Column(name="email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name="qr_img")
    public String getQrImg() {
        return qrImg;
    }

    public void setQrImg(String qrImg) {
        this.qrImg = qrImg;
    }

    @Column(name="tel")
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Column(name="is_delete")
    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    @Column(name="img")
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Column(name="detail")
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Column(name="gender")
    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    @Column(name="age")
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Column(name="gid")
    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    @Column(name="nickname")
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Column(name="create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getHid() {
        return hid;
    }

    @Column(name="hosname")
    public String getHospitalName() {
        return hospitalName;
    }

    @Column(name="depaerment")
    public String getDepartment() {
        return department;
    }

    @Column(name="depaerment_tel")
    public String getDepartmentTel() {
        return departmentTel;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public void setHid(Long hid) {
        this.hid = hid;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setDepartmentTel(String departmentTel) {
        this.departmentTel = departmentTel;
    }

    @Column(length=2048)
    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public static Finder<Long, Doctor> find = new Finder<>(Long.class, Doctor.class);


    public static List<Doctor> search(String filter,int page,int pageSize) {
        return getPage(filter, page, pageSize).getList();
    }

    public static int count(String filter, int page,int pageSize) {
        return getPage(filter, page, pageSize).getTotalRowCount();
    }


    private static Page<Doctor> getPage(String filter,int page,int pageSize){
        Logger.debug("search Doctor with param [{}]", filter, page, pageSize);

        ExpressionList<Doctor> expression = Doctor.find.where();

        if (filter != null && !"".equals(filter)) {
            expression.or(
                    Expr.like("doctorName", "%" + filter + "%"),
                    Expr.like("nickname", "%" + filter + "%")
            );
        }

        expression.eq("deleted",0);

        return expression
                .orderBy("id desc")
                .findPagingList(pageSize)
                .setFetchAhead(false)
                .getPage(page);
    }


}
