(:
    Generated by Apache ServiceMix Archetype
:)
<titles>
{
  for $x in /bookstore/book
  where $x/price > 30
  return $x/title
}
</titles>
