import { Center, MantineProvider, Stack, Title } from "@mantine/core"
import ky from "ky"
import { useEffect, useState } from "react"
import { z } from "zod"
import BookCreator from "./components/BookCreator.js"
import BooksDisplay from "./components/BooksDisplay.js"
import { theme } from "./theme.js"

export const Book = z.object({
  id: z.number().int().positive(),
  title: z.string().min(1),
  author: z.string().min(1),
  favorite: z.boolean(),
})
export type TBook = z.infer<typeof Book>

export const Books = Book.array().transform((books) => books.sort((a, b) => a.id - b.id))
export type TBooks = z.infer<typeof Books>

export const URL = "http://localhost:8080"

const App = () => {
  const [books, setBooks] = useState<TBooks>([])

  const getAndSetBooks = async () => {
    const response = await ky.get(`${URL}/books`).json()
    setBooks(Books.parse(response))
  }

  useEffect(() => void getAndSetBooks(), [])

  return (
    <MantineProvider theme={theme} withGlobalStyles withNormalizeCSS>
      <Center>
        <Stack align="center">
          <Title mt="lg" mb="lg">
            My Library
          </Title>
          <BookCreator books={books} setBooks={setBooks} />
          <BooksDisplay books={books} setBooks={setBooks} />
        </Stack>
      </Center>
    </MantineProvider>
  )
}

export default App
