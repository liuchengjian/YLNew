package lchj.ylnews.mvp.model.entity;

public class LoginBody {
    private Integer type;
    private String phone;
    private String code;
    private String password;
    public LoginBody(int type,String phone,String password,String code){
        this.type = type;
        if(type==1){
            this.phone = phone;
            this.code = code;
        }else {
            this.phone = phone;
            this.password = password;
        }
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
