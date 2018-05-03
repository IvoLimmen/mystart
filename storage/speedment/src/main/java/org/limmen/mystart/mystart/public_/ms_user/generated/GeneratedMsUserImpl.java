package org.limmen.mystart.mystart.public_.ms_user.generated;

import com.speedment.runtime.core.util.OptionalUtil;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import javax.annotation.Generated;
import org.limmen.mystart.mystart.public_.ms_user.MsUser;

/**
 * The generated base implementation of the {@link
 * org.limmen.mystart.mystart.public_.ms_user.MsUser}-interface.
 * <p>
 * This file has been automatically generated by Speedment. Any changes made to
 * it will be overwritten.
 * 
 * @author Speedment
 */
@Generated("Speedment")
public abstract class GeneratedMsUserImpl implements MsUser {
    
    private int id;
    private String email;
    private String password;
    
    protected GeneratedMsUserImpl() {
        
    }
    
    @Override
    public int getId() {
        return id;
    }
    
    @Override
    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }
    
    @Override
    public Optional<String> getPassword() {
        return Optional.ofNullable(password);
    }
    
    @Override
    public MsUser setId(int id) {
        this.id = id;
        return this;
    }
    
    @Override
    public MsUser setEmail(String email) {
        this.email = email;
        return this;
    }
    
    @Override
    public MsUser setPassword(String password) {
        this.password = password;
        return this;
    }
    
    @Override
    public String toString() {
        final StringJoiner sj = new StringJoiner(", ", "{ ", " }");
        sj.add("id = "       + Objects.toString(getId()));
        sj.add("email = "    + Objects.toString(OptionalUtil.unwrap(getEmail())));
        sj.add("password = " + Objects.toString(OptionalUtil.unwrap(getPassword())));
        return "MsUserImpl " + sj.toString();
    }
    
    @Override
    public boolean equals(Object that) {
        if (this == that) { return true; }
        if (!(that instanceof MsUser)) { return false; }
        final MsUser thatMsUser = (MsUser)that;
        if (this.getId() != thatMsUser.getId()) {return false; }
        if (!Objects.equals(this.getEmail(), thatMsUser.getEmail())) {return false; }
        if (!Objects.equals(this.getPassword(), thatMsUser.getPassword())) {return false; }
        return true;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Integer.hashCode(getId());
        hash = 31 * hash + Objects.hashCode(getEmail());
        hash = 31 * hash + Objects.hashCode(getPassword());
        return hash;
    }
}