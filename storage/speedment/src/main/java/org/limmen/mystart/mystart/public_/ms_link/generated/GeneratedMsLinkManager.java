package org.limmen.mystart.mystart.public_.ms_link.generated;

import com.speedment.runtime.core.manager.Manager;
import javax.annotation.Generated;
import org.limmen.mystart.mystart.public_.ms_link.MsLink;

/**
 * The generated base interface for the manager of every {@link
 * org.limmen.mystart.mystart.public_.ms_link.MsLink} entity.
 * <p>
 * This file has been automatically generated by Speedment. Any changes made to
 * it will be overwritten.
 * 
 * @author Speedment
 */
@Generated("Speedment")
public interface GeneratedMsLinkManager extends Manager<MsLink> {
    
    @Override
    default Class<MsLink> getEntityClass() {
        return MsLink.class;
    }
}