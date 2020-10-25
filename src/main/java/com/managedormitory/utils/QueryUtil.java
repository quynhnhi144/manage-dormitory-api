package com.managedormitory.utils;

public class QueryUtil {
    private QueryUtil() {
    }

    public static final String LEFTJOIN_DETAILROOM = "left join detail_room dr on dr.student_id = s.id\n";
    public static final String LEFTJOIN_STUDENT = "left join student s on r.id = s.room_id\n";
    public static final String LEFTJOIN_WATERBILL = "left join water_bill wb on s.id = wb.student_id\n";
    public static final String LEFTJOIN_POWERBILL = "left join power_bill pb on r.id = pb.room_id\n";
    public static final String LEFTJOIN_VEHICLE = "left join vehicle v on s.id = v.student_id\n";
    public static final String LEFTJOIN_VEHICLEBILL = "left join vehicle_bill vb on v.id = vb.vehicle_id\n";
    public static final String LEFTJOIN_TYPEROOM = "left join type_room tr on r.type_room_id = tr.id\n";
    public static final String JOIN_CAMPUS = "join campus c on c.id = r.campus_id\n";
    public static final String JOIN_USERS = "join users u on u.user_id = c.user_id\n";
    public static final String JOIN_PRICELIST = "join price_list pl on pl.id = r.price_list_id\n";
    public static final String JOIN_ROOM = "join room r on r.id = s.room_id\n";
    public static final String JOIN_STUDENT = "join student s on s.id = v.student_id\n";
    public static final String JOIN_TYPEVEHICLE = "join type_vehicle tv on v.type_vehicle_id = tv.id\n";
}
