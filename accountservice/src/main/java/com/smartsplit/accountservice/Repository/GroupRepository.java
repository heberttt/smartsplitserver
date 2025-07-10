package com.smartsplit.accountservice.Repository;

import java.util.List;
import java.util.Optional;

import com.smartsplit.accountservice.DO.GroupDO;

public interface GroupRepository {
    public Optional<GroupDO> findGroupById(int id);

    public List<GroupDO> findGroupsByUserId(String userId);

    public void createGroup(GroupDO group);

    public void joinGroup(int groupId, String userId);

    public void deleteGroup(int groupId);
}
