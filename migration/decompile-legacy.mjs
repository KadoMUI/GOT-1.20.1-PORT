import fs from "node:fs";
import path from "node:path";
import { createRequire } from "node:module";

const require = createRequire(import.meta.url);
const { decompile } = require("/tmp/vf-decompiler/node_modules/@run-slicer/vf/vf.js");

const [classRoot, outputRoot, ...targets] = process.argv.slice(2);
if (!classRoot || !outputRoot || targets.length === 0) {
  throw new Error("usage: node decompile-legacy.mjs CLASS_ROOT OUTPUT_ROOT CLASS...");
}

const normalize = (name) => name.replaceAll(".", "/").replace(/\.class$/, "");
const wanted = targets.map(normalize);

const result = await decompile(wanted, {
  source: async (name) => {
    const file = path.join(classRoot, `${name}.class`);
    return fs.existsSync(file) ? fs.readFileSync(file) : null;
  },
  resources: [],
  options: { banner: "// Decompiled from the original GPL-licensed GOT 1.7.10 mod.\n" },
  logger: {
    writeMessage() {},
    writeMessageWithThrowable() {},
    startReadingClass() {},
    endReadingClass() {},
    startClass() {},
    endClass() {},
    startMethod() {},
    endMethod() {},
  },
});

for (const target of wanted) {
  const source = result[target];
  if (source === undefined) {
    throw new Error(`No decompiler output for ${target}`);
  }
  const file = path.join(outputRoot, `${target}.java`);
  fs.mkdirSync(path.dirname(file), { recursive: true });
  fs.writeFileSync(file, source);
}
