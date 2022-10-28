import { Table } from "@mantine/core"
import { FC } from "react"
import { TBooks } from "../App.js"
import BookDisplay from "./BookDisplay.js"

interface BooksDisplayProps {
  books: TBooks
  setBooks: React.Dispatch<React.SetStateAction<TBooks>>
}

const BookList: FC<BooksDisplayProps> = ({ books, setBooks }) => {
  return (
    <div>
      <Table highlightOnHover striped withBorder horizontalSpacing="md" verticalSpacing="md" fontSize="xl">
        <thead>
          <tr>
            <th></th>
            <th>ID</th>
            <th>Title</th>
            <th>Author</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {books.map((book) => (
            <BookDisplay key={book.id} book={book} setBooks={setBooks} />
          ))}
        </tbody>
      </Table>
    </div>
  )
}

export default BookList
