package com.fsy.api.entity;/**
 * Created by zln on 2017/11/29.
 */

/**
 * 描述:
 *
 * @auth zln
 * @create 2017-11-29 16:08
 */
public class ClientUser {

    private Integer id;
    private String username; //用户名
    private String password; //密码
    private String email;//邮箱
    private String id_type;//证件类型
    private String id_card;//证件号
    private String phone;//手机号
    private String address;//地址
    private Integer status;//用户状态
    private String sms_contact;//受信人
    private String create_date;//创建时间
    private Integer type;//类型
    private String weixin;
    private String sex;//性别
    private String bloodType;//血型
    private String medicalHistory;
    private String contraindicated;
    private String beneficiary;
    private String birthday;
    private String career;
    private Double client_balance;
    private Double client_invoice_balance;
    private Integer ruibaoVip;
    private String default_dangerous;


    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getId_type() {
        return id_type;
    }
    public void setId_type(String id_type) {
        this.id_type = id_type;
    }
    public String getId_card() {
        return id_card;
    }
    public void setId_card(String id_card) {
        this.id_card = id_card;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getCreate_date() {
        return create_date;
    }
    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public String getWeixin() {
        return weixin;
    }
    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }
    public String getBloodType() {
        return bloodType;
    }
    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }
    public String getMedicalHistory() {
        return medicalHistory;
    }
    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }
    public String getContraindicated() {
        return contraindicated;
    }
    public void setContraindicated(String contraindicated) {
        this.contraindicated = contraindicated;
    }
    public String getBeneficiary() {
        return beneficiary;
    }
    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }
    public String getBirthday() {
        return birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public String getCareer() {
        return career;
    }
    public void setCareer(String career) {
        this.career = career;
    }
    public String getSms_contact() {
        return sms_contact;
    }
    public void setSms_contact(String sms_contact) {
        this.sms_contact = sms_contact;
    }
    public Double getClient_balance() {
        return client_balance;
    }
    public void setClient_balance(Double client_balance) {
        this.client_balance = client_balance;
    }
    public Double getClient_invoice_balance() {
        return client_invoice_balance;
    }
    public void setClient_invoice_balance(Double client_invoice_balance) {
        this.client_invoice_balance = client_invoice_balance;
    }
    public Integer getRuibaoVip() {
        return ruibaoVip;
    }
    public void setRuibaoVip(Integer ruibaoVip) {
        this.ruibaoVip = ruibaoVip;
    }
    public String getDefault_dangerous() {
        return default_dangerous;
    }
    public void setDefault_dangerous(String default_dangerous) {
        this.default_dangerous = default_dangerous;
    }
}
