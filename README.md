## wordnetd

Wordnetd is an ultra-fast in-memory Wordnet server. It may be used in situations where you need to access Wordnet fast by avoiding to touch the hard drive as much as you can. With wordnetd, the data is aggressively cached in memory.

### Usage

Using wordnetd is simple. Just ```telnet localhost 4444``` (the default port in use) and then the protocol is as follows:

| Command  | Param (optional) | Description |
| -------- | ---------------- | ----------- |
| synonym  | noun/verb        | Returns the synonyms. If a param is entered, returns verbs or nouns only. |
| hypernym | noun/verb        | Returns the hypernyms. If a param is entered, returns verbs or nouns only. |
| polysemy |         | Returns the polysemies.|
| top_polysemy |       | Returns the top polysemy.  |
| bye      |                  | Quits       |
