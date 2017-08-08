package bz.dcr.deinprotect.config;

public interface LangKey {

    String PREFIX = "Prefix";
    String DATE_FORMAT = "Date-Format";
    String NUMBER_FORMAT = "Number-Format";
    String NUMBER_FORMAT_DECIMAL_SEPARATOR = "Number-Format-Decimal-Separator";
    String NUMBER_FORMAT_GROUPINGL_SEPARATOR = "Number-Format-Grouping-Separator";

    String PROTECTION_CREATED = "Status.Protection-Created";
    String PROTECTION_DELETED = "Status.Protection-Deleted";
    String MEMBER_ADDED = "Status.Member-Added";
    String MEMBER_REMOVED = "Status.Member-Removed";

    String ERR_PLAYER_NOT_EXISTING = "Error.Player-Not-Existing";
    String ERR_PLAYER_IS_MEMBER = "Error.Player-Is-Member";
    String ERR_PLAYER_IS_NOT_MEMBER = "Error.Player-Is-Not-Member";

    String GUI_MAIN_MENU_TITLE = "GUI.Main-Menu.Title";
    String GUI_MAIN_MENU_ADD_MEMBER = "GUI.Main-Menu.Add-Member";
    String GUI_MAIN_MENU_MEMBERS = "GUI.Main-Menu.Members";
    String GUI_MAIN_MENU_REMOVE_MEMBER = "GUI.Main-Menu.Remove-Member";
    String GUI_MAIN_MENU_PUBLIC_PERMS = "GUI.Main-Menu.Public-Permissions";
    String GUI_MAIN_MENU_DELETE_PROTECTION = "GUI.Main-Menu.Delete-Protection";

    String GUI_MEMBERS_TITLE = "GUI.Members.Title";
    String GUI_ADD_MEMBER_MESSAGE = "GUI.Add-Member.Message";
    String GUI_REMOVE_MEMBER_MESSAGE = "GUI.Remove-Member.Message";

    String GUI_PERMISSIONS_TITLE = "GUI.Permissions.Title";
    String GUI_PERMISSIONS_ON = "Protection-Permission.Prefix-On";
    String GUI_PERMISSIONS_OFF = "Protection-Permission.Prefix-Off";

}
