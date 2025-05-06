#!/usr/bin/env node

import * as fs from 'fs';
import * as jsonld from 'jsonld';

export default async function main() {
    const nquads = fs.readFileSync('./notes.nq').toString()
    const jsonLdArr = await jsonld.fromRDF(nquads, { format: 'application/n-quads' });
    const jsonLdStr = JSON.stringify(jsonLdArr, null, 4);
    fs.writeFileSync('./notes.jsonld', jsonLdStr);
}

main();