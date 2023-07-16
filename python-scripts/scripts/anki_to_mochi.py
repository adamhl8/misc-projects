import re
import orjson
from pathlib import Path

whitespace_pattern = re.compile(r"\s+")

def process_value(card, field: str, review_list):
    value: str = card["~:fields"][field]["~:value"]
    value = whitespace_pattern.sub(" ", value).strip()
    original_value = value

    value = value.replace("\[", "}(")
    value = value.replace("\]", ")")

    value = " " + value
    matches: list[str] = re.findall(r'\s.*?}', value)

    # Sometimes the start of the string looks like: " X Y}"
    # In which we would have matched that whole start instead of just " Y}"
    for index, match in enumerate(matches):
      match = match.lstrip()
      new_match = re.search(r'\s.*?}', match)
      if new_match is not None:
        matches[index] = new_match.group(0)

    for match in matches:
        match = match.lstrip()
        match = " " + match # make sure there's only 1 leading space
        if match.startswith(" **"):
          replaced = match.replace("**", "**{")
          value = value.replace(match, replaced)
        else:
          replaced = match.replace(" ", " {")
          value = value.replace(match, replaced)

    value = value.strip()

    length_mismatch = len(value) != len(original_value)
    not_closed = (value.count("{") + value.count("}")) % 2 != 0 or (value.count("(") + value.count(")")) % 2 != 0
    if length_mismatch or not_closed:
      review_list.append((original_value, value))

    value = whitespace_pattern.sub("", value)

    return value

def print_review(name, review_list):
  print(f"{name} to fix:")
  for ele in review_list:
    print(ele[0])
    print(ele[1])
    print()

data = orjson.loads(Path("data.json").read_bytes())
cards = data["~:decks"][0]["~:cards"]["~#list"]

sentences_to_review = []
readings_to_review = []
for index, card in enumerate(cards):
    sentence = process_value(card, "~:4BgmbWla", sentences_to_review)
    card["~:fields"]["~:4BgmbWla"]["~:value"] = sentence

    reading = process_value(card, "~:4dpslJUC", readings_to_review)
    card["~:fields"]["~:4dpslJUC"]["~:value"] = reading

    cards[index] = card

data["~:decks"][0]["~:cards"]["~#list"] = cards
Path("new_data.json").write_bytes(orjson.dumps(data))

print_review("Sentences", sentences_to_review)
print_review("Readings", readings_to_review)
