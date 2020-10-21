package com.managedormitory.utils;

public class QueryUtil {
    private QueryUtil() {
    }

    public static final StringBuilder QUERY_ALL_ROOMS = new StringBuilder().append("select r.id as id,\n")
            .append("r.name as name,\n")
            .append("r.quantity_student as quantityStudent,\n")
            .append("tr.name as typeRoomName,\n")
            .append("c.name as campusName,\n")
            .append("u.full_name as userManager,\n")
            .append("dr.is_pay as isPay\n")
            .append("from room r\n")
            .append("  join type_room tr on r.type_room_id = tr.id\n")
            .append("  join campus c on c.id = r.campus_id\n")
            .append("  join users u on u.user_id = c.user_id\n")
            .append("  left join detail_room dr on r.id = dr.room_id\n");
}
