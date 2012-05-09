package org.mattstep.platform.samples.contact;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableSet;

import javax.inject.Inject;
import java.util.Set;

public class ContactStore
{
    private final LoadingCache<String, Set<String>> contactCache;

    private static final Set<String> CONTACTS = ImmutableSet.of("martint", "electrum", "mattstep", "dphillips");

    @Inject
    public ContactStore()
    {
        contactCache = CacheBuilder.newBuilder()
                .build(new CacheLoader<String, Set<String>>() {

                    @Override
                    public Set<String> load(String key)
                            throws Exception
                    {
                        return CONTACTS;
                    }
                });
    }

    public Set<String> getAllContactsForOwner(String ownerId)
    {
        return contactCache.getUnchecked(ownerId);
    }
}
