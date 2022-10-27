import { useEffect, useState } from "react"
import "./App.css.js.js.js"
import ky from "ky"
import { z } from "zod"
import ItemList from "./components/ItemList.js"
import ItemCreator from "./components/ItemCreator.js"

export const Item = z.object({
  id: z.number().int().positive(),
  content: z.string().min(1),
  completed: z.boolean(),
})
export type TItem = z.infer<typeof Item>

export const Items = Item.array()
export type TItems = z.infer<typeof Items>

export const URL = "http://localhost:8080/api"

function App() {
  const [items, setItems] = useState<TItems>([])

  const getItems = async () => {
    const response = await ky.get(`${URL}/items`).json()
    setItems(Items.parse(response))
  }

  useEffect(() => void getItems(), [])

  return (
    <>
      <ItemCreator items={items} setItems={setItems} />
      <ItemList items={items} setItems={setItems} />
    </>
  )
}

export default App
