package com.guhao.star.skills;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import yesman.epicfight.api.data.reloader.SkillManager;
import yesman.epicfight.api.forgeevent.SkillBuildEvent;
import yesman.epicfight.skill.Skill;

import static com.guhao.star.Star.MODID;

public class StarSkill {
    public static Skill SEE_THROUGH;

    public StarSkill() {
    }

    public static void registerSkills() {
        SkillManager.register(SeeThroughSkill::new, SeeThroughSkill.createSeeThroughSkillBuilder(), MODID, "see_through");
    }
    public static void BuildSkills(SkillBuildEvent event) {
        Logger LOGGER = LogUtils.getLogger();
        LOGGER.info("Build Star Skill");
        SEE_THROUGH = event.build(MODID, "see_through");
    }
}
