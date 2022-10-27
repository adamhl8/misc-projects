import { FC, useState } from "react"
import { TItems, Item } from "../App.js"
import ky from "ky"
import { URL } from "../App.js"

interface ItemCreatorProps {
  items: TItems
  setItems: React.Dispatch<React.SetStateAction<TItems>>
}

const ItemCreator: FC<ItemCreatorProps> = ({ items, setItems }) => {
  const [inputValue, setInputValue] = useState("")

  const handleSubmit = async () => {
    const newTask = {
      content: inputValue,
      completed: false,
    }
    const response = await ky.post(`${URL}/items`, { json: newTask }).json()
    setItems([...items, Item.parse(response)])

    const input = document.querySelector<HTMLInputElement>("#newTaskInput")
    if (!input) return
    input.value = ""
  }

  return (
    <div>
      <form
        onSubmit={(event) => {
          event.preventDefault()
          void handleSubmit()
        }}
      >
        <label>
          <span>New Task: </span>
          <input id="newTaskInput" type="text" onChange={(event) => setInputValue(event.target.value)} />
        </label>
        <input type="submit" />
      </form>
    </div>
  )
}

export default ItemCreator
