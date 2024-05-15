package top.secundario.gamma.common;

public class CMExample {
    private static CfgModel createCfgModel() {
        CfgModel cfgModel = new CfgModel("My Smart Home");

        CfgNode cnRoom1 = new CfgNode("Room 1", 1, cfgModel);
        CfgNode cnRoom2 = new CfgNode("Room 2", 2, cfgModel);
        CfgNode cnWashRoom = new CfgNode("Wash Room", 3, cfgModel);

        CfgNode cnRoom1_lighting = new CfgNode("Room 1 - Lighting", 11, cnRoom1);
        CfgNode cnRoom1_invadeDetect = new CfgNode("Room 1 - Invade Detect", 12, cnRoom1);

        CfgItem cnRoom1_lighting_lamp1 = new CfgItem("Room 1 - Lighting - Lamp 1", 111, cnRoom1_lighting);
        CfgItem cnRoom1_lighting_lamp2 = new CfgItem("Room 1 - Lighting - Lamp 2", 112, cnRoom1_lighting);
        CfgItem cnRoom1_lighting_switch1 = new CfgItem("Room 1 - Lighting - Switch 1", 113, cnRoom1_lighting);
        CfgItem cnRoom1_lighting_switch2 = new CfgItem("Room 1 - Lighting - Switch 2", 114, cnRoom1_lighting);

        CfgItem cnRoom1_invadeDetect_sensor = new CfgItem("Room 1 - Invade Detect - Sensor", 121, cnRoom1_invadeDetect);
        CfgItem cnRoom1_invadeDetect_alarm = new CfgItem("Room 1 - Invade Detect - Alarm", 122, cnRoom1_invadeDetect);

        CfgNode cnRoom2_lighting = new CfgNode("Room 2 - Lighting", 21, cnRoom2);

        CfgItem cnRoom2_lighting_lamp = new CfgItem("Room 2 - Lighting - Lamp", 211, cnRoom2_lighting);
        CfgItem cnRoom2_lighting_switch = new CfgItem("Room 2 - Lighting - Switch", 212, cnRoom2_lighting);

        CfgNode cnWashRoom_invadeDetect = new CfgNode("Wash Room - Invade Detect", 31, cnWashRoom);

        CfgItem cnWashRoom_invadeDetect_sensor = new CfgItem("Wash Room - Invade Detect - Sensor", 311, cnWashRoom_invadeDetect);
        CfgItem cnWashRoom_invadeDetect_alarm = new CfgItem("Wash Room - Invade Detect - Alarm", 312, cnWashRoom_invadeDetect);

        return cfgModel;
    }

    private static void example1() {
        CfgModel cm = createCfgModel();
        cm.simpleWalk(System.out::println);
    }
    
    public static void main(String[] args) {
        example1();
    }
}
