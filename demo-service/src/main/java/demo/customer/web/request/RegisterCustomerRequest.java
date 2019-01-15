package demo.customer.web.request;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author neo
 */
@XmlRootElement(name = "register_customer_request")
@XmlAccessorType(XmlAccessType.FIELD)
public class RegisterCustomerRequest {
    @NotBlank
    @Length(max = 20, message = "max length of name is {max}")
    public String name;
    @NotBlank
    @Email(message = "must be in email format")
    @Length(max = 100, message = "max length of email is {max}")
    public String email;
}
