package main.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.model.FightOutcome;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FightResult {

    private FightOutcome outcome;

    private String mobName;

    private int xpEarned;

    private int adenaEarned;
}
