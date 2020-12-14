package com.managedormitory.utils;

public class StringUtil {
    private StringUtil() {
    }

    public static String QUOTE = "'";
    public static String PERCENTAGE = "%";
    public static String FILE_NAME_EXCEL_ROOM = "rooms.xlsx";
    public static String DOT_STAR = ".*";
    public static String[] HEADER_POWER_BILLS = {"Id", "Tên Phòng", "Ngày Bắt Đầu", "Ngày Kết Thúc", "Chỉ Số Đầu", "Chỉ Số Cuối", "Chỉ Số Sử Dụng", "Số Tiền Phải Trả", "Đã Trả"};
    public static String[] HEADER_STUDENTS = {"Id", "Mã số sinh viên", "Tên sinh viên", "Số điện thoại", "Địa chỉ", "Phòng", "Đã trả tiền phòng", "Đã trả tiền nước", "Còn ở"};
    public static String[] HEADER_VEHICLES = {"Id", "Biển số xe", "Loại xe", "Tên sinh viên", "Đã trả", "Còn sử dụng"};
    public static String SHEET_ROOM = "Phòng";
    public static String SHEET_POWERBILL = "Hóa đơn điện";
    public static String SHEET_STUDENT = "Sinh viên";
    public static String SHEET_VEHICLE = "Xe";
    public static String SHEET_USER = "Nhân viên";
    public static String FILE_NAME_EXCEL_POWER_BILL = "powerBills.xlsx";
    public static String FILE_NAME_EXCEL_VEHICLE = "vehicles.xlsx";
    public static String MEDIA_TYPE = "application/vnd.ms-excel";

    public static String KTXBK = "KÝ TÚC XÁ BÁCH KHOA";
    public static String CHXHCNVN = "CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM";
    public static String DLTDHP = "Độc lập - Tự do - Hạnh phúc";
    public static String TITLE = "PHIẾU THANH TOÁN";
    public static final String PATH_TO_FONT = "/font/arialuni.ttf";
    public static final String SUBJECT_SUCCESS_REGISTER_ROOM = "Thông tin về đăng ký phòng";
    public static final String CONTENT_SUCCESS_REGISTER_ROOM = "Chúc mừng bạn đã đăng ký thành công \nBạn hãy đến trung tâm ký túc xá để đóng tiền trong thời gian sớm nhất.\nNếu không thì đăng ký của bạn sẽ bị hủy";
    public static final String CONTENT_SUCCESS_ADD_STUDENT = "Chúc mừng bạn đã trở thành một sinh viên sinh sống tại ký túc xá";
    public static final String CONTENT_REJECT_REGISTER_ROOM = "Rất tiếc đăng ký của bạn đã bị hủy";
}
