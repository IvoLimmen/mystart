package org.limmen.mystart.mystart.public_.ms_user.generated;

import com.speedment.runtime.core.manager.Manager;
import javax.annotation.Generated;
import org.limmen.mystart.mystart.public_.ms_user.MsUser;

/**
 * The generated base interface for the manager of every {@link
 * org.limmen.mystart.mystart.public_.ms_user.MsUser} entity.
 * <p>
 * This file has been automatically generated by Speedment. Any changes made to
 * it will be overwritten.
 * 
 * @author Speedment
 */
@Generated("Speedment")
public interface GeneratedMsUserManager extends Manager<MsUser> {
    
    @Override
    default Class<MsUser> getEntityClass() {
        return MsUser.class;
    }
}