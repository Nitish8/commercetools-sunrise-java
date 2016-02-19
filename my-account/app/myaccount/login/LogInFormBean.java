package myaccount.login;

import common.errors.ErrorsBean;
import io.sphere.sdk.models.Base;

public class LogInFormBean extends Base {
    private ErrorsBean errors;
    private String username;

    public LogInFormBean() {
    }

    public LogInFormBean(final LogInFormData formData) {
        this.username = formData.getUsername();
    }

    public ErrorsBean getErrors() {
        return errors;
    }

    public void setErrors(final ErrorsBean errors) {
        this.errors = errors;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }
}
