//@ts-check
import { EventList, Player, ScriptManager } from "menscript";

var scriptmanager = new ScriptManager("TPScript", "1.0", ["Exterminate"]);
var script = scriptmanager.getScript();
var module = script.registerModule("TP", "A module that teleports you up.");
var heightSetting = module.addSliderSetting("Height", true, 5, 1, 10, true);

module.hook(EventList.ONENABLE, function (event) {
    Player.setY(Player.getY() + heightSetting.getValue());
});