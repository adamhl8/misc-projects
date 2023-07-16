import { TItems } from "../App.js"
import ItemDisplay from "./ItemDisplay.js"

interface ItemsProps {
  items: TItems
  setItems: React.Dispatch<React.SetStateAction<TItems>>
}

const ItemList = (props: ItemsProps) => {
  return (
    <div>
      {props.items.map((item) => (
        <ItemDisplay key={item.id} item={item} setItems={props.setItems} />
      ))}
    </div>
  )
}

export default ItemList
