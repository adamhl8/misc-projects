import { DeleteOutlined, StarFilled, StarOutlined } from "@ant-design/icons"
import { Text } from "@mantine/core"
import ky from "ky"
import { FC } from "react"
import { Books, TBook, TBooks, URL } from "../App.js"

interface BookDisplayProps {
  book: TBook
  setBooks: React.Dispatch<React.SetStateAction<TBooks>>
}

const BookDisplay: FC<BookDisplayProps> = ({ book, setBooks }) => {
  const toggleFavorite = async () => {
    await ky.patch(`${URL}/books/${book.id}`, { json: { favorite: !book.favorite } }).json()
    const response = await ky.get(`${URL}/books`).json()
    setBooks(Books.parse(response))
  }

  const removeBook = async () => {
    await ky.delete(`${URL}/books/${book.id}`).json()
    const response = await ky.get(`${URL}/books`).json()
    setBooks(Books.parse(response))
  }

  return (
    <tr>
      <td>
        {book.favorite ? <StarFilled onClick={() => void toggleFavorite()} /> : <StarOutlined onClick={() => void toggleFavorite()} />}
      </td>
      <td>{book.id}</td>
      <td>
        <Text color={book.favorite ? "green" : ""}>{book.title}</Text>
      </td>
      <td>{book.author}</td>
      <td>
        <DeleteOutlined onClick={() => void removeBook()} />
      </td>
    </tr>
  )
}

export default BookDisplay
