import ky from "ky"
import { TItem, Items, TItems, URL } from "../App.js"

interface ItemProps {
  item: TItem
  setItems: React.Dispatch<React.SetStateAction<TItems>>
}

const ItemDisplay = (props: ItemProps) => {
  const toggleCompleted = async () => {
    const patch = await ky.patch(`${URL}/items/${props.item.id}`, { json: { completed: !props.item.completed } }).json()
    console.log(patch)
    const response = await ky.get(`${URL}/items`).json()
    props.setItems(Items.parse(response))
  }

  const deleteItem = async () => {
    await ky.delete(`${URL}/items/${props.item.id}`)
    const response = await ky.get(`${URL}/items`).json()
    props.setItems(Items.parse(response))
  }

  return (
    <div>
      <button onClick={() => void toggleCompleted()}>{props.item.completed ? "☑" : "☐"}</button>
      &nbsp;
      {props.item.completed ? <s>{props.item.content}</s> : props.item.content}
      &nbsp;
      <button onClick={() => void deleteItem()}>delete</button>
    </div>
  )
}

export default ItemDisplay
