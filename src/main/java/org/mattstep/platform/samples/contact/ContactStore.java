package org.mattstep.platform.samples.contact;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.eclipse.jetty.util.ConcurrentHashSet;

import javax.inject.Inject;
import java.util.Set;

public class ContactStore
{
    private final LoadingCache<String, Set<String>> contactCache;

    @Inject
    public ContactStore()
    {
        contactCache = CacheBuilder.newBuilder()
                .build(new CacheLoader<String, Set<String>>() {

                    @Override
                    public Set<String> load(String key)
                            throws Exception
                    {
                        return new ConcurrentHashSet<String>();
                    }
                });
    }

    public Set<String> getAllContactsForOwner(String ownerId)
    {
        Preconditions.checkNotNull(ownerId);

        return contactCache.getUnchecked(ownerId);
    }

    public void addContact(String ownerId, String contactId)
    {
        Preconditions.checkNotNull(ownerId);
        Preconditions.checkNotNull(contactId);

        contactCache.getUnchecked(ownerId).add(contactId);
    }

    public RemovalStatus removeContact(String ownerId, String contactId)
    {
        Preconditions.checkNotNull(ownerId);
        Preconditions.checkNotNull(contactId);

        return contactCache.getUnchecked(ownerId).remove(contactId) ? RemovalStatus.REMOVED : RemovalStatus.NOT_PRESENT;
    }

    public enum RemovalStatus
    {
        REMOVED, NOT_PRESENT
    }
}
