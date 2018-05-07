package com.yht.iptv.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by admin on 2017/8/21.
 */

@Table(name = "MallLoginInfo")
public class MallLoginInfo {

    /**
     * aagentblack : 0
     * aagentlevel : 0
     * aagentnotupgrade : 0
     * aagentstatus : 0
     * aagenttime : 0
     * aagenttype : 0
     * agentblack : 0
     * agentid : 0
     * agentlevel : 0
     * agentnotupgrade : 0
     * agentselectgoods : 0
     * agenttime : 0
     * area :
     * authorblack : 0
     * authorid : 0
     * authorlevel : 0
     * authornotupgrade : 0
     * authorstatus : 0
     * authortime : 0
     * avatar : http://wx.qlogo.cn/mmopen/ajNVdqHZLLADqNt0OQibaHWgEEKmjh19xia8iann7ISYXfuyaItG5ErUGjEbn8Td817dKOSCzgTmjlRbLJqQjGbsZf6Iwnztxy4niaDkMClUYibI/132
     * avatar_wechat : http://wx.qlogo.cn/mmopen/ajNVdqHZLLADqNt0OQibaHWgEEKmjh19xia8iann7ISYXfuyaItG5ErUGjEbn8Td817dKOSCzgTmjlRbLJqQjGbsZf6Iwnztxy4niaDkMClUYibI/132
     * birthday :
     * birthmonth :
     * birthyear :
     * carrier_mobile : 0
     * childtime : 0
     * city :
     * clickcount : 0
     * commission_total : 0.00
     * createtime : 1503222435
     * credit1 : 0.00
     * credit2 : 0.00
     * datavalue :
     * diyaagentid : 0
     * diyauthorid : 0
     * diycommissiondataid : 0
     * diycommissionid : 0
     * diyglobonusid : 0
     * diymaxcredit : 0
     * diymemberdataid : 0
     * diymemberid : 0
     * endtime2 : 0
     * fixagentid : 0
     * gender : 1
     * groupid : 0
     * hotelID : 1001
     * id : 2208
     * inviter : 0
     * isaagent : 0
     * isagent : 0
     * isauthor : 0
     * isblack : 0
     * ispartner : 0
     * level : 0
     * maxcredit : 0
     * mobile :
     * mobileuser : 0
     * mobileverify : 0
     * nickname : kinder.zhuang
     * nickname_wechat : kinder.zhuang
     * openid : oDTLU0nmD_krdxrnultMeCvpU_HE
     * partnerblack : 0
     * partnerlevel : 0
     * partnernotupgrade : 0
     * partnerstatus : 0
     * partnertime : 0
     * province :
     * pwd :
     * realname :
     * roomNum : 202
     * status : 0
     * uid : 2
     * uniacid : 3
     * updateaddress : 1
     * userID : 202
     * username :
     * weixin :
     */

    private String aagentblack;
    private String aagentlevel;
    private String aagentnotupgrade;
    private String aagentstatus;
    private String aagenttime;
    private String aagenttype;
    private String agentblack;
    private String agentid;
    private String agentlevel;
    private String agentnotupgrade;
    private String agentselectgoods;
    private String agenttime;
    private String area;
    private String authorblack;
    private String authorid;
    private String authorlevel;
    private String authornotupgrade;
    private String authorstatus;
    private String authortime;
    private String avatar;
    private String avatar_wechat;
    private String birthday;
    private String birthmonth;
    private String birthyear;
    private String carrier_mobile;
    private String childtime;
    private String city;
    private String clickcount;
    private String commission_total;
    private String createtime;
    private String credit1;
    private String credit2;
    private String datavalue;
    private String diyaagentid;
    private String diyauthorid;
    private String diycommissiondataid;
    private String diycommissionid;
    private String diyglobonusid;
    private String diymaxcredit;
    private String diymemberdataid;
    private String diymemberid;
    private String endtime2;
    private String fixagentid;
    private String gender;
    private String groupid;
    private String hotelID;
    @Column(name = "id" ,isId = true)
    private String id;
    private String inviter;
    private String isaagent;
    private String isagent;
    private String isauthor;
    private String isblack;
    private String ispartner;
    private String level;
    private String maxcredit;
    private String mobile;
    private String mobileuser;
    private String mobileverify;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "nickname_wechat")
    private String nickname_wechat;
    @Column(name = "openid")
    private String openid;
    private String partnerblack;
    private String partnerlevel;
    private String partnernotupgrade;
    private String partnerstatus;
    private String partnertime;
    private String province;
    private String pwd;
    private String realname;
    private String roomNum;
    private String status;
    private String uid;
    private String uniacid;
    private String updateaddress;
    private String userID;
    private String username;
    private String weixin;

    public String getAagentblack() {
        return aagentblack;
    }

    public void setAagentblack(String aagentblack) {
        this.aagentblack = aagentblack;
    }

    public String getAagentlevel() {
        return aagentlevel;
    }

    public void setAagentlevel(String aagentlevel) {
        this.aagentlevel = aagentlevel;
    }

    public String getAagentnotupgrade() {
        return aagentnotupgrade;
    }

    public void setAagentnotupgrade(String aagentnotupgrade) {
        this.aagentnotupgrade = aagentnotupgrade;
    }

    public String getAagentstatus() {
        return aagentstatus;
    }

    public void setAagentstatus(String aagentstatus) {
        this.aagentstatus = aagentstatus;
    }

    public String getAagenttime() {
        return aagenttime;
    }

    public void setAagenttime(String aagenttime) {
        this.aagenttime = aagenttime;
    }

    public String getAagenttype() {
        return aagenttype;
    }

    public void setAagenttype(String aagenttype) {
        this.aagenttype = aagenttype;
    }

    public String getAgentblack() {
        return agentblack;
    }

    public void setAgentblack(String agentblack) {
        this.agentblack = agentblack;
    }

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }

    public String getAgentlevel() {
        return agentlevel;
    }

    public void setAgentlevel(String agentlevel) {
        this.agentlevel = agentlevel;
    }

    public String getAgentnotupgrade() {
        return agentnotupgrade;
    }

    public void setAgentnotupgrade(String agentnotupgrade) {
        this.agentnotupgrade = agentnotupgrade;
    }

    public String getAgentselectgoods() {
        return agentselectgoods;
    }

    public void setAgentselectgoods(String agentselectgoods) {
        this.agentselectgoods = agentselectgoods;
    }

    public String getAgenttime() {
        return agenttime;
    }

    public void setAgenttime(String agenttime) {
        this.agenttime = agenttime;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAuthorblack() {
        return authorblack;
    }

    public void setAuthorblack(String authorblack) {
        this.authorblack = authorblack;
    }

    public String getAuthorid() {
        return authorid;
    }

    public void setAuthorid(String authorid) {
        this.authorid = authorid;
    }

    public String getAuthorlevel() {
        return authorlevel;
    }

    public void setAuthorlevel(String authorlevel) {
        this.authorlevel = authorlevel;
    }

    public String getAuthornotupgrade() {
        return authornotupgrade;
    }

    public void setAuthornotupgrade(String authornotupgrade) {
        this.authornotupgrade = authornotupgrade;
    }

    public String getAuthorstatus() {
        return authorstatus;
    }

    public void setAuthorstatus(String authorstatus) {
        this.authorstatus = authorstatus;
    }

    public String getAuthortime() {
        return authortime;
    }

    public void setAuthortime(String authortime) {
        this.authortime = authortime;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar_wechat() {
        return avatar_wechat;
    }

    public void setAvatar_wechat(String avatar_wechat) {
        this.avatar_wechat = avatar_wechat;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthmonth() {
        return birthmonth;
    }

    public void setBirthmonth(String birthmonth) {
        this.birthmonth = birthmonth;
    }

    public String getBirthyear() {
        return birthyear;
    }

    public void setBirthyear(String birthyear) {
        this.birthyear = birthyear;
    }

    public String getCarrier_mobile() {
        return carrier_mobile;
    }

    public void setCarrier_mobile(String carrier_mobile) {
        this.carrier_mobile = carrier_mobile;
    }

    public String getChildtime() {
        return childtime;
    }

    public void setChildtime(String childtime) {
        this.childtime = childtime;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getClickcount() {
        return clickcount;
    }

    public void setClickcount(String clickcount) {
        this.clickcount = clickcount;
    }

    public String getCommission_total() {
        return commission_total;
    }

    public void setCommission_total(String commission_total) {
        this.commission_total = commission_total;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCredit1() {
        return credit1;
    }

    public void setCredit1(String credit1) {
        this.credit1 = credit1;
    }

    public String getCredit2() {
        return credit2;
    }

    public void setCredit2(String credit2) {
        this.credit2 = credit2;
    }

    public String getDatavalue() {
        return datavalue;
    }

    public void setDatavalue(String datavalue) {
        this.datavalue = datavalue;
    }

    public String getDiyaagentid() {
        return diyaagentid;
    }

    public void setDiyaagentid(String diyaagentid) {
        this.diyaagentid = diyaagentid;
    }

    public String getDiyauthorid() {
        return diyauthorid;
    }

    public void setDiyauthorid(String diyauthorid) {
        this.diyauthorid = diyauthorid;
    }

    public String getDiycommissiondataid() {
        return diycommissiondataid;
    }

    public void setDiycommissiondataid(String diycommissiondataid) {
        this.diycommissiondataid = diycommissiondataid;
    }

    public String getDiycommissionid() {
        return diycommissionid;
    }

    public void setDiycommissionid(String diycommissionid) {
        this.diycommissionid = diycommissionid;
    }

    public String getDiyglobonusid() {
        return diyglobonusid;
    }

    public void setDiyglobonusid(String diyglobonusid) {
        this.diyglobonusid = diyglobonusid;
    }

    public String getDiymaxcredit() {
        return diymaxcredit;
    }

    public void setDiymaxcredit(String diymaxcredit) {
        this.diymaxcredit = diymaxcredit;
    }

    public String getDiymemberdataid() {
        return diymemberdataid;
    }

    public void setDiymemberdataid(String diymemberdataid) {
        this.diymemberdataid = diymemberdataid;
    }

    public String getDiymemberid() {
        return diymemberid;
    }

    public void setDiymemberid(String diymemberid) {
        this.diymemberid = diymemberid;
    }

    public String getEndtime2() {
        return endtime2;
    }

    public void setEndtime2(String endtime2) {
        this.endtime2 = endtime2;
    }

    public String getFixagentid() {
        return fixagentid;
    }

    public void setFixagentid(String fixagentid) {
        this.fixagentid = fixagentid;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getHotelID() {
        return hotelID;
    }

    public void setHotelID(String hotelID) {
        this.hotelID = hotelID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInviter() {
        return inviter;
    }

    public void setInviter(String inviter) {
        this.inviter = inviter;
    }

    public String getIsaagent() {
        return isaagent;
    }

    public void setIsaagent(String isaagent) {
        this.isaagent = isaagent;
    }

    public String getIsagent() {
        return isagent;
    }

    public void setIsagent(String isagent) {
        this.isagent = isagent;
    }

    public String getIsauthor() {
        return isauthor;
    }

    public void setIsauthor(String isauthor) {
        this.isauthor = isauthor;
    }

    public String getIsblack() {
        return isblack;
    }

    public void setIsblack(String isblack) {
        this.isblack = isblack;
    }

    public String getIspartner() {
        return ispartner;
    }

    public void setIspartner(String ispartner) {
        this.ispartner = ispartner;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMaxcredit() {
        return maxcredit;
    }

    public void setMaxcredit(String maxcredit) {
        this.maxcredit = maxcredit;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobileuser() {
        return mobileuser;
    }

    public void setMobileuser(String mobileuser) {
        this.mobileuser = mobileuser;
    }

    public String getMobileverify() {
        return mobileverify;
    }

    public void setMobileverify(String mobileverify) {
        this.mobileverify = mobileverify;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname_wechat() {
        return nickname_wechat;
    }

    public void setNickname_wechat(String nickname_wechat) {
        this.nickname_wechat = nickname_wechat;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getPartnerblack() {
        return partnerblack;
    }

    public void setPartnerblack(String partnerblack) {
        this.partnerblack = partnerblack;
    }

    public String getPartnerlevel() {
        return partnerlevel;
    }

    public void setPartnerlevel(String partnerlevel) {
        this.partnerlevel = partnerlevel;
    }

    public String getPartnernotupgrade() {
        return partnernotupgrade;
    }

    public void setPartnernotupgrade(String partnernotupgrade) {
        this.partnernotupgrade = partnernotupgrade;
    }

    public String getPartnerstatus() {
        return partnerstatus;
    }

    public void setPartnerstatus(String partnerstatus) {
        this.partnerstatus = partnerstatus;
    }

    public String getPartnertime() {
        return partnertime;
    }

    public void setPartnertime(String partnertime) {
        this.partnertime = partnertime;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(String roomNum) {
        this.roomNum = roomNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUniacid() {
        return uniacid;
    }

    public void setUniacid(String uniacid) {
        this.uniacid = uniacid;
    }

    public String getUpdateaddress() {
        return updateaddress;
    }

    public void setUpdateaddress(String updateaddress) {
        this.updateaddress = updateaddress;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }
}
