package com.smartsplit.accountservice.Repository.Implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.smartsplit.accountservice.DO.AccountDO;
import com.smartsplit.accountservice.DO.GroupDO;
import com.smartsplit.accountservice.Repository.GroupRepository;

@Repository
public class GroupRepositoryImpl implements GroupRepository {

    private final JdbcClient jdbcClient;

    public GroupRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Optional<GroupDO> findGroupById(int id) {
        Optional<GroupDO> optionalGroup = jdbcClient
                .sql("SELECT id, name FROM groups WHERE id = :id")
                .param("id", id)
                .query(GroupDO.class)
                .optional();

        if (optionalGroup.isEmpty()) {
            return Optional.empty();
        }

        GroupDO group = optionalGroup.get();

        List<AccountDO> members = jdbcClient
                .sql("""
                            SELECT a.id, a.username, a.email, a.profilePictureLink
                            FROM group_members gm
                            JOIN accounts a ON gm.account_id = a.id
                            WHERE gm.group_id = :groupId
                        """)
                .param("groupId", id)
                .query(AccountDO.class)
                .list();

        group.setMembers(members);
        return Optional.of(group);
    }

    @Override
    public List<GroupDO> findGroupsByUserId(String userId) {
        List<GroupDO> groups = jdbcClient
                .sql("""
                            SELECT g.id, g.name
                            FROM groups g
                            JOIN group_members gm ON g.id = gm.group_id
                            WHERE gm.account_id = :userId
                        """)
                .param("userId", userId)
                .query((rs, rowNum) -> {
                    GroupDO group = new GroupDO();
                    group.setId(Integer.parseInt(rs.getString("id")));
                    group.setName(rs.getString("name"));
                    return group;
                })
                .list();

        for (GroupDO group : groups) {
            List<AccountDO> members = jdbcClient
                    .sql("""
                                SELECT a.id, a.username, a.email, a.profilePictureLink
                                FROM accounts a
                                JOIN group_members gm ON a.id = gm.account_id
                                WHERE gm.group_id = :groupId
                            """)
                    .param("groupId", group.getId())
                    .query((rs, rowNum) -> {
                        AccountDO account = new AccountDO();
                        account.setId(rs.getString("id"));
                        account.setUsername(rs.getString("username"));
                        account.setEmail(rs.getString("email"));
                        account.setProfilePictureLink(rs.getString("profilePictureLink"));
                        return account;
                    })
                    .list();

            group.setMembers(members);
        }

        return groups;
    }

    @Override
    public void createGroup(GroupDO group) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient
                .sql("INSERT INTO groups (name) VALUES (:name)")
                .param("name", group.getName())
                .update(keyHolder, "id");

        Number key = keyHolder.getKey();
        if (key != null) {
            int groupId = key.intValue();
            group.setId(groupId);

            if (group.getMembers() != null) {
                for (AccountDO member : group.getMembers()) {
                    jdbcClient
                            .sql("INSERT INTO group_members (group_id, account_id) VALUES (:groupId, :accountId)")
                            .param("groupId", groupId)
                            .param("accountId", member.getId())
                            .update();
                }
            }
        } else {
            throw new IllegalStateException("Failed to retrieve generated group ID.");
        }
    }

    @Override
    public void joinGroup(int groupId, String userId) {
        jdbcClient
                .sql("INSERT INTO group_members (group_id, account_id) VALUES (:groupId, :userId)")
                .param("groupId", groupId)
                .param("userId", userId)
                .update();
    }

    @Override
    public void deleteGroup(int groupId) {
        jdbcClient
                .sql("DELETE FROM group_members WHERE group_id = :groupId")
                .param("groupId", groupId)
                .update();

        jdbcClient
                .sql("DELETE FROM groups WHERE id = :groupId")
                .param("groupId", groupId)
                .update();
    }

    @Override
    public void leaveGroup(int groupId, String userId) {
        final int result = jdbcClient
                .sql("DELETE FROM group_members WHERE group_id = :groupId AND account_id = :accountId")
                .param("groupId", groupId)
                .param("accountId", userId)
                .update();

        Assert.state(result == 1, "Failed to leave group: " + userId);
    }

    

}
