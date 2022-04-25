// Simple Node.js script for generating a word import SQL-script
const { readFileSync, writeFileSync } = require("fs");

const LINGO_WORDS = /^[a-z]{5,7}$/;
const words = readFileSync(`${__dirname}/woorden-opentaal.txt`, "utf-8")
    .split("\n")
    .filter(word => LINGO_WORDS.test(word))
    .map(word => `('${word}', ${word.length})`);

const sql = "CREATE TABLE words (\n"
    + "    word varchar(16) PRIMARY KEY,\n"
    + "    length smallint\n"
    + "); \n\n"
    + "-- Extracted from: https://github.com/OpenTaal/opentaal-wordlist/\n"
    + `INSERT INTO words (word, length) VALUES \n\t${words.join(",\n\t")};`;

writeFileSync(`${__dirname}/../db/lingo_words.sql`, sql);
