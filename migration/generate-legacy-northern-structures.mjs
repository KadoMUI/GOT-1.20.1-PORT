import fs from "node:fs";
import path from "node:path";

const root = path.resolve(import.meta.dirname, "..");
const legacy = path.join(root, "legacy-src/got/common/world/structure/westeros");
const output = path.join(root, "src/main/java/got/world/structure/legacy/generated");

const specs = [
  ...["Barn", "Bath", "Cottage", "Fortress", "Gatehouse", "House", "LampPost", "MarketStall",
      "Obelisk", "Smithy", "Stables", "StoneHouse", "Tavern", "Tower", "TownBench", "TownGarden",
      "TownTrees", "VillageFarm", "VillageSign", "Watchfort", "Watchtower", "Well"]
    .map(name => ({ file: `common/GOTStructureWesteros${name}.java`, name: `GOTStructureWesteros${name}` })),
  { file: "common/GOTStructureWesterosTownWall.java", name: "GOTStructureWesterosTownWall", custom: "townWall" },
  ...["Animals", "Crops", "Tree"].map(name => ({
    file: `common/GOTStructureWesterosVillageFarm$${name}.java`,
    name: `GOTStructureWesterosVillageFarm$${name}`,
    extends: "GOTStructureWesterosVillageFarm"
  })),
  ...["Gate", "House", "HouseSmall", "Smithy", "Stables", "VillageLight", "VillagePalisade", "Well", "Castle"]
    .map(name => ({ file: `gift/GOTStructureGift${name}.java`, name: `GOTStructureGift${name}` })),
  ...["House", "ChieftainHouse", "Barn", "Keep"]
    .map(name => ({ file: `wildling/GOTStructureWildling${name}.java`, name: `GOTStructureWildling${name}` })),
  ...["House", "ChieftainHouse"]
    .map(name => ({ file: `wildling/thenn/GOTStructureThenn${name}.java`, name: `GOTStructureThenn${name}` })),
  ...["House", "ChieftainHouse"]
    .map(name => ({ file: `north/hillmen/GOTStructureNorthHillman${name}.java`, name: `GOTStructureNorthHillman${name}` })),
];

function matchingBrace(text, open) {
  let depth = 0;
  for (let i = open; i < text.length; i++) {
    if (text[i] === "{") depth++;
    else if (text[i] === "}" && --depth === 0) return i;
  }
  throw new Error(`Unmatched brace at ${open}`);
}

function removeConstructors(body, className) {
  const escaped = className.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
  const pattern = new RegExp(`(?:public|protected|private)\\s+${escaped}\\s*\\(`, "g");
  for (;;) {
    const match = pattern.exec(body);
    if (!match) return body;
    const open = body.indexOf("{", match.index);
    const semi = body.indexOf(";", match.index);
    if (open < 0 || (semi >= 0 && semi < open)) {
      body = body.slice(0, match.index) + body.slice(semi + 1);
    } else {
      body = body.slice(0, match.index) + body.slice(matchingBrace(body, open) + 1);
    }
    pattern.lastIndex = 0;
  }
}

function removeNamedMethods(body, names) {
  for (const name of names) {
    const pattern = new RegExp(`(?:public|protected|private)\\s+(?:static\\s+)?[^;{}]+\\s+${name}\\s*\\(`, "g");
    for (;;) {
      const match = pattern.exec(body);
      if (!match) break;
      const open = body.indexOf("{", match.index);
      const semi = body.indexOf(";", match.index);
      if (open < 0 || (semi >= 0 && semi < open)) {
        body = body.slice(0, match.index) + body.slice(semi + 1);
      } else {
        body = body.slice(0, match.index) + body.slice(matchingBrace(body, open) + 1);
      }
      pattern.lastIndex = 0;
    }
  }
  return body;
}

function transform(source, spec) {
  const declaration = source.match(new RegExp(`(?:public\\s+)?(?:abstract\\s+)?class\\s+${spec.name.replace(/\$/g, "\\$")}\\b[^\\{]*\\{`));
  if (!declaration) throw new Error(`No class body for ${spec.name}`);
  const open = source.indexOf("{", declaration.index);
  let body = source.slice(open + 1, matchingBrace(source, open));
  body = removeConstructors(body, spec.name);
  if (spec.custom === "townWall") {
    body = removeNamedMethods(body, ["Centre", "Left", "LeftEnd", "LeftEndShort", "Right", "RightEnd", "RightEndShort"]);
  }
  body = body.replace(/^\s*@Override\s*$/gm, "");
  body = body.replace(/^\s*protected\s+abstract\s+[^;]+;\s*$/gm, "");

  body = body
    .replace(/\bGOTBlocks\.([A-Za-z0-9_]+)/g, 'got("$1")')
    .replace(/\bBlocks\.([A-Za-z0-9_]+)/g, 'vanilla("$1")')
    .replace(/\bGOTChestContents\.([A-Za-z0-9_]+)/g, '"chest_$1"')
    .replace(/\bGOTFoods\.([A-Za-z0-9_]+)/g, '"food_$1"')
    .replace(/\bBannerType\.([A-Za-z0-9_]+)/g, '"banner_$1"')
    .replace(/\bGOTItems\.([A-Za-z0-9_]+)/g, '"item_$1"')
    .replace(/\bItems\.([A-Za-z0-9_]+)/g, '"item_$1"')
    .replace(/\bIntMath\.mod\s*\(/g, "Math.floorMod(")
    .replace(/\bGOTEntity[A-Za-z0-9_$]+\.class/g, "LegacyEntity.class")
    .replace(/\bGOTStructureBase\b/g, "LegacyNorthernContext")
    .replace(/\bIBlockAccess\b/g, "LegacyNorthernContext")
    .replace(/\bGOTFarmhand\b/g, "LegacyEntity")
    .replace(/\bGOTEntity[A-Za-z0-9_$]*\b/g, "LegacyEntity")
    .replace(/\bLancelLannisterNormal\b/g, "LegacyEntity")
    .replace(/\bEntity(?:Animal|Chicken|Cow|Pig|Sheep|Horse)\b/g, "LegacyEntity")
    .replace(/\bItemStack\b/g, "LegacyItemStack")
    .replace(/\bBlock\b/g, "LegacyBlock")
    .replace(/\bWorld\b/g, "LegacyNorthernContext");

  body = body.replace(/GOTNames\.getTavernName\(random\)/g, "getTavernName(random)");
  body = body.replace(/LegacyItemStack flower = this\.getRandomFlower/g,
    "LegacyBlock flower = this.getRandomFlower");
  body = body.replace(/LegacyBlock\.func_149634_a\(flower\.func_77973_b\(\)\),\s*flower\.func_77960_j\(\)/g,
    "flower, 0");
  body = body.replace(/int var53 = false;/g, "boolean var53 = false;");
  body = body.replace(/\s*GOTTreeType tree;[\s\S]*?treeGen\.func_76484_a\([^;]+;\s*/,
    "\n      this.placeFarmTree(world, random);\n");

  // Population methods are intentionally markers in this milestone.
  body = body.replace(/this\.spawnLegendaryNPC\((new LegacyEntity\([^;]+?\)),\s*world,\s*([^;]+?)\);/g,
    'this.spawnLegendaryNPC($1, world, $2);');

  // The watchfort's attached beacon tower is a child structure, not an API call.
  body = body.replace(/\s*GOTStructureWesterosTower beaconTower[\s\S]*?beaconTower\.generateAndHeight\([^;]+;\s*/,
    "\n      this.placeBeaconTower(-4, 17, 9, 0, 5);\n");

  const style = spec.file.startsWith("gift/") ? "Style.GIFT"
    : spec.file.startsWith("wildling/") ? "Style.WILDLING" : "Style.NORTH";
  const flags = spec.custom === "townWall" ? `
   public static void place(NorthStructureBuilder builder, int variant) {
      GOTStructureWesterosTownWall piece = new GOTStructureWesterosTownWall(builder, variant);
      piece.generate(piece, piece.legacyRandom(), 0, 0, 0, 0);
   }
` : `
   public static void place(NorthStructureBuilder builder, int variant) {
      ${spec.name} piece = new ${spec.name}(builder);
      piece.marketVariant = Math.max(0, variant);
      piece.isAbandoned = variant == 1;
      piece.isTramp = variant == 1;
      piece.isHardhome = variant == 1;
      piece.isBlacksmith = variant == 1;
      piece.generate(piece, piece.legacyRandom(), 0, 0, 0, 0);
   }
`;

  const parent = spec.extends ?? "LegacyNorthernContext";
  const superCall = spec.extends ? "super(builder);" : `super(builder, ${style});`;
  const constructor = spec.custom === "townWall" ? `
   protected ${spec.name}(NorthStructureBuilder builder, int variant) {
      super(builder, ${style});
      int x0;
      int x1;
      int xi0;
      int xi1;
      switch (variant) {
         case 1 -> { x0 = -9; x1 = 6; xi0 = x0; xi1 = x1; }
         case 2 -> { x0 = -6; x1 = 6; xi0 = -5; xi1 = 6; }
         case 3 -> { x0 = -5; x1 = 6; xi0 = x0; xi1 = x1; }
         case 4 -> { x0 = -6; x1 = 9; xi0 = x0; xi1 = x1; }
         case 5 -> { x0 = -6; x1 = 6; xi0 = -6; xi1 = 5; }
         case 6 -> { x0 = -6; x1 = 5; xi0 = x0; xi1 = x1; }
         default -> { x0 = -5; x1 = 5; xi0 = x0; xi1 = x1; }
      }
      this.xMin = x0;
      this.xMax = x1;
      this.xMinInner = xi0;
      this.xMaxInner = xi1;
   }
` : `
   protected ${spec.name}(NorthStructureBuilder builder) {
      ${superCall}
   }
`;
  return `package got.world.structure.legacy.generated;

import got.world.structure.legacy.LegacyNorthernContext;
import got.world.structure.legacy.LegacyNorthernContext.LegacyBlock;
import got.world.structure.legacy.LegacyNorthernContext.LegacyEntity;
import got.world.structure.legacy.LegacyNorthernContext.LegacyItemStack;
import got.world.structure.north.NorthStructureBuilder;
import java.util.ArrayList;
import java.util.Random;

/** Exact coordinate body mechanically recovered from the GPL 1.7.10 source. */
public class ${spec.name} extends ${parent} {
${constructor}
${flags}
${body}
}
`;
}

fs.mkdirSync(output, { recursive: true });
for (const spec of specs) {
  const source = fs.readFileSync(path.join(legacy, spec.file), "utf8");
  fs.writeFileSync(path.join(output, `${spec.name}.java`), transform(source, spec));
}
console.log(`Generated ${specs.length} exact legacy northern pieces in ${output}`);
