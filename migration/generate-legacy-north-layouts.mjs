import fs from "node:fs";
import path from "node:path";

const root = path.resolve(import.meta.dirname, "..");
const input = path.join(root, "legacy-src/got/common/world/structure/westeros/north/GOTStructureNorthSettlement$Instance.java");
const output = path.join(root, "src/main/java/got/world/structure/north/LegacyNorthSettlementLayouts.java");
const source = fs.readFileSync(input, "utf8");

function matching(text, open, left = "{", right = "}") {
  let depth = 0;
  for (let i = open; i < text.length; i++) {
    if (text[i] === left) depth++;
    else if (text[i] === right && --depth === 0) return i;
  }
  throw new Error(`Unmatched ${left} at ${open}`);
}

function methodBody(name) {
  const match = source.match(new RegExp(`private void ${name}\\((?:Random random)?\\)\\s*\\{`));
  if (!match) throw new Error(`Missing ${name}`);
  const open = source.indexOf("{", match.index);
  return source.slice(open + 1, matching(source, open));
}

function splitArguments(value) {
  const out = [];
  let start = 0, round = 0, square = 0, curly = 0;
  for (let i = 0; i < value.length; i++) {
    if (value[i] === "(") round++;
    else if (value[i] === ")") round--;
    else if (value[i] === "[") square++;
    else if (value[i] === "]") square--;
    else if (value[i] === "{") curly++;
    else if (value[i] === "}") curly--;
    else if (value[i] === "," && round === 0 && square === 0 && curly === 0) {
      out.push(value.slice(start, i).trim()); start = i + 1;
    }
  }
  out.push(value.slice(start).trim());
  return out;
}

function token(expression) {
  if (expression.includes("StructureRespawner")) return "RESPAWNER";
  if (expression.includes("getRandomStall")) return "MARKET";
  if (expression.includes("getRandomHouse")) return "RANDOM_HOUSE";
  if (expression.includes("getRandomFarm")) return "RANDOM_FARM";
  const townWall = expression.match(/GOTStructureWesterosTownWall\.(Centre|LeftEndShort|LeftEnd|Left|RightEndShort|RightEnd|Right)\(/);
  if (townWall) return `TOWN_WALL_${townWall[1].replace(/([a-z])([A-Z])/g, "$1_$2").toUpperCase()}`;
  if (expression.includes("HayBales")) return "HAY";
  if (/new Tree\(/.test(expression)) return "FARM_TREE";
  if (/new Crops\(/.test(expression)) return "FARM_CROPS";
  const match = expression.match(/new\s+([A-Za-z0-9_$]+)/);
  if (!match) throw new Error(`Unknown structure expression: ${expression}`);
  let result = match[1].replace(/^GOTStructure/, "");
  if (expression.includes("setIsTramp")) result += "_SPECIAL";
  return result;
}

function replacePlacements(body) {
  const needle = "this.addStructure(";
  for (;;) {
    const start = body.indexOf(needle);
    if (start < 0) break;
    const open = start + needle.length - 1;
    const close = matching(body, open, "(", ")");
    const semi = body.indexOf(";", close);
    const args = splitArguments(body.slice(open + 1, close));
    if (args.length < 4) throw new Error(`Bad addStructure: ${body.slice(start, semi + 1)}`);
    const replacement = `place(b, "${token(args[0])}", ${args[1]}, ${args[2]}, ${args[3]}, random);`;
    body = body.slice(0, start) + replacement + body.slice(semi + 1);
  }
  return body
    .replace(/IntMath\.mod\(/g, "Math.floorMod(")
    .replace(/MathHelper\.func_76126_a\(/g, "(float)Math.sin(")
    .replace(/MathHelper\.func_76134_b\(/g, "(float)Math.cos(");
}

const methods = ["setupVillage", "setupHillman", "setupSmallTown", "setupTown"]
  .map(name => {
    const publicName = name.replace(/^setup/, "");
    const suffix = "";
    return `public static void ${publicName.substring(0,1).toLowerCase() + publicName.substring(1)}(NorthStructureBuilder b) {
      Random random = new Random(b.seed());
${replacePlacements(methodBody(name))}${suffix}
   }`;
  }).join("\n\n   ");

const java = `package got.world.structure.north;

import java.util.Random;
import net.minecraft.world.level.block.Blocks;

/** Settlement piece coordinates mechanically recovered from the legacy Instance class. */
public final class LegacyNorthSettlementLayouts {
   private LegacyNorthSettlementLayouts() {}

   ${methods}

   private static void place(NorthStructureBuilder parent, String piece, int x, int z,
                             int rotation, Random random) {
      NorthStructureBuilder b = parent.child(x, 0, z, rotation, random.nextLong());
      switch (piece) {
         case "RESPAWNER" -> b.marker("north_population_respawner", 0, 0, 0);
         case "MARKET" -> got.world.structure.legacy.generated.GOTStructureWesterosMarketStall.place(b, random.nextInt(11));
         case "RANDOM_HOUSE" -> {
            if (random.nextInt(5) == 0) switch (random.nextInt(3)) {
               case 0 -> got.world.structure.legacy.generated.GOTStructureWesterosStables.place(b, 0);
               case 1 -> got.world.structure.legacy.generated.GOTStructureWesterosSmithy.place(b, 0);
               default -> got.world.structure.legacy.generated.GOTStructureWesterosBarn.place(b, 0);
            } else got.world.structure.legacy.generated.GOTStructureWesterosHouse.place(b, 0);
         }
         case "RANDOM_FARM" -> {
            int farm = random.nextBoolean() ? (random.nextBoolean() ? 1 : 0) : 2;
            NorthStructureTemplates.villageFarm(b, farm);
         }
         case "FARM_TREE" -> got.world.structure.legacy.generated.GOTStructureWesterosVillageFarm$Tree.place(b, 0);
         case "FARM_CROPS" -> got.world.structure.legacy.generated.GOTStructureWesterosVillageFarm$Crops.place(b, 0);
         case "HAY" -> b.fill(-2, 0, -2, 2, 2, 2, Blocks.HAY_BLOCK.defaultBlockState());
         case "WesterosWell", "MossovyWell" -> got.world.structure.legacy.generated.GOTStructureWesterosWell.place(b, 0);
         case "WesterosLampPost" -> got.world.structure.legacy.generated.GOTStructureWesterosLampPost.place(b, 0);
         case "WesterosObelisk" -> got.world.structure.legacy.generated.GOTStructureWesterosObelisk.place(b, 0);
         case "WesterosTownBench" -> got.world.structure.legacy.generated.GOTStructureWesterosTownBench.place(b, 0);
         case "WesterosTownGarden" -> got.world.structure.legacy.generated.GOTStructureWesterosTownGarden.place(b, 0);
         case "WesterosTownTrees" -> got.world.structure.legacy.generated.GOTStructureWesterosTownTrees.place(b, 0);
         case "WesterosVillageSign" -> got.world.structure.legacy.generated.GOTStructureWesterosVillageSign.place(b, 0);
         case "NorthHouse" -> got.world.structure.legacy.generated.GOTStructureWesterosCottage.place(b, 0);
         case "NorthHouseLarge" -> got.world.structure.legacy.generated.GOTStructureWesterosStoneHouse.place(b, 0);
         case "NorthHouseSmall" -> got.world.structure.legacy.generated.GOTStructureWesterosHouse.place(b, 0);
         case "NorthTavern" -> got.world.structure.legacy.generated.GOTStructureWesterosTavern.place(b, 0);
         case "NorthSmithy" -> got.world.structure.legacy.generated.GOTStructureWesterosSmithy.place(b, 0);
         case "NorthBarn" -> got.world.structure.legacy.generated.GOTStructureWesterosBarn.place(b, 0);
         case "NorthBath" -> got.world.structure.legacy.generated.GOTStructureWesterosBath.place(b, 0);
         case "NorthGatehouse" -> got.world.structure.legacy.generated.GOTStructureWesterosGatehouse.place(b, 0);
         case "NorthWatchtower" -> got.world.structure.legacy.generated.GOTStructureWesterosWatchtower.place(b, 0);
         case "NorthHillmanHouse" -> got.world.structure.legacy.generated.GOTStructureNorthHillmanHouse.place(b, 0);
         case "NorthHillmanHouse_SPECIAL" -> got.world.structure.legacy.generated.GOTStructureNorthHillmanHouse.place(b, 1);
         case "NorthHillmanChieftainHouse" -> got.world.structure.legacy.generated.GOTStructureNorthHillmanChieftainHouse.place(b, 0);
         case "TOWN_WALL_CENTRE" -> got.world.structure.legacy.generated.GOTStructureWesterosTownWall.place(b, 0);
         case "TOWN_WALL_LEFT" -> got.world.structure.legacy.generated.GOTStructureWesterosTownWall.place(b, 1);
         case "TOWN_WALL_LEFT_END" -> got.world.structure.legacy.generated.GOTStructureWesterosTownWall.place(b, 2);
         case "TOWN_WALL_LEFT_END_SHORT" -> got.world.structure.legacy.generated.GOTStructureWesterosTownWall.place(b, 3);
         case "TOWN_WALL_RIGHT" -> got.world.structure.legacy.generated.GOTStructureWesterosTownWall.place(b, 4);
         case "TOWN_WALL_RIGHT_END" -> got.world.structure.legacy.generated.GOTStructureWesterosTownWall.place(b, 5);
         case "TOWN_WALL_RIGHT_END_SHORT" -> got.world.structure.legacy.generated.GOTStructureWesterosTownWall.place(b, 6);
         default -> throw new IllegalArgumentException("Unmapped recovered North piece: " + piece);
      }
   }
}
`;

fs.writeFileSync(output, java);
console.log(`Generated recovered North settlement layouts at ${output}`);
