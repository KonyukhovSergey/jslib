package js.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

public class TreeItem
{
	private enum State
	{
		S01_SPACE_BEFORE_NAME,
		S02_NAME,
		S03_SPACE_BEFORE_VALUE,
		S04_VALUE
	}

	private String name;
	private String value;

	private TreeItem next;
	private TreeItem child;

	public TreeItem()
	{
	}

	public TreeItem(TreeItem item)
	{
		name = item.name;
		value = item.value;
		next = item.next;
		child = item.child;
	}

	public TreeItem(String name, String value)
	{
		this.name = name;
		this.value = value;
	}

	public TreeItem add(TreeItem childItem)
	{
		if (this.child == null)
		{
			this.child = childItem;
		}
		else
		{
			TreeItem item = this.child;

			while (item.next != null)
			{
				item = item.next;
			}

			item.next = childItem;
		}

		return childItem;
	}

	public TreeItem add(int position, TreeItem childItem)
	{
		if (position == 0)
		{
			childItem.next = this.child;
			this.child = childItem;
		}
		else
		{
			TreeItem item = this.child;

			if (item == null)
			{
				return null;
			}

			while (--position > 0)
			{
				item = item.next;

				if (item == null)
				{
					return null;
				}
			}

			childItem.next = item.next;
			item.next = childItem;
		}

		return childItem;
	}

	public int size()
	{
		int size = 0;

		TreeItem item = child;

		while (item != null)
		{
			size++;
			item = item.next;
		}

		return size;
	}

	public List<TreeItem> find(String name)
	{
		List<TreeItem> list = new ArrayList<TreeItem>();

		find(name, list);

		return list;
	}

	public void find(String name, List<TreeItem> list)
	{
		list.clear();

		TreeItem item = this.child;

		while (item != null)
		{
			if (item.name.equals(name))
			{
				list.add(item);
			}

			item = item.next;
		}
	}

	public TreeItem contain(String name)
	{
		TreeItem item = this.child;

		while (item != null)
		{
			if (item.name.equals(name))
			{
				return item;
			}

			item = item.next;
		}

		return null;
	}

	public TreeItem child(int index)
	{
		TreeItem item = this.child;

		while (index > 0 && item != null)
		{
			item = item.next;
			index--;
		}

		return item;
	}

	public String child(String name, String defaultValue)
	{
		TreeItem item = contain(name);

		if (item != null)
		{
			return item.value;
		}

		return defaultValue;
	}

	public String name()
	{
		return name;
	}

	public void name(String name)
	{
		this.name = name;
	}

	public void read(InputStreamReader stream) throws IOException
	{
		StringBuilder sb = new StringBuilder();

		State state = State.S01_SPACE_BEFORE_NAME;

		int c;

		TreeItem item = this;
		TreeItem prev = null;

		while ((c = stream.read()) >= 0)
		{
			switch (state)
			{
				case S01_SPACE_BEFORE_NAME:

					if (c == '}')
					{
						if (prev != null)
						{
							prev.next = null;
						}
						return;
					}

					if (!Character.isWhitespace(c))
					{
						sb.setLength(0);
						sb.append((char) c);
						state = State.S02_NAME;
					}
					break;

				case S02_NAME:
					if (Character.isWhitespace(c))
					{
						item.name = sb.toString();
						state = State.S03_SPACE_BEFORE_VALUE;
					}
					else
					{
						sb.append((char) c);
					}
					break;

				case S03_SPACE_BEFORE_VALUE:
					if (!Character.isWhitespace(c))
					{
						sb.setLength(0);
						sb.append((char) c);
						state = State.S04_VALUE;
					}
					break;

				case S04_VALUE:
					if (c == ';')
					{
						item.value = sb.toString().trim();

						item.next = new TreeItem();
						prev = item;
						item = item.next;
						state = State.S01_SPACE_BEFORE_NAME;
						continue;
					}

					if (c == '{')
					{
						item.value = sb.toString().trim();

						item.child = new TreeItem();
						item.child.read(stream);

						item.next = new TreeItem();
						prev = item;
						item = item.next;
						state = State.S01_SPACE_BEFORE_NAME;

						continue;
					}

					sb.append((char) c);

					break;

				default:
					break;
			}
		}

		if (prev != null)
		{
			prev.next = null;
		}
	}

	public TreeItem remove(String name)
	{
		if (this.child == null)
		{
			return null;
		}

		TreeItem item = this.child;

		if (item.name.equals(name))
		{
			this.child = item.next;
			return item;
		}

		while (item.next != null)
		{
			if (item.next.name.equals(name))
			{
				TreeItem removedItem = item.next;
				item.next = item.next.next;
				removedItem.next = null;
				return removedItem;
			}

			item = item.next;
		}

		return null;
	}

	public String value()
	{
		return value;
	}

	public void value(String value)
	{
		this.value = value;
	}

	public void set(String name, String value)
	{
		TreeItem item = contain(name);

		if (item == null)
		{
			add(new TreeItem(name, value));
		}
		else
		{
			item.value(value);
		}
	}

	public void write(OutputStreamWriter stream, int level) throws IOException
	{
		TreeItem item = this;

		String tab = new String("\t");

		while (item != null)
		{
			write(stream, level, tab);

			if (item.child != null)
			{
				stream.write(String.format("%s %s\r\n", item.name, item.value));
				write(stream, level, tab);
				stream.write(String.format("{\r\n"));
				item.child.write(stream, level + 1);
				write(stream, level, tab);
				stream.write(String.format("}\r\n"));
			}
			else
			{
				stream.write(String.format("%s %s;\r\n", item.name, item.value));
			}

			item = item.next;
		}
	}

	private void write(OutputStreamWriter stream, int level, String tab) throws IOException
	{
		for (int i = 0; i < level; i++)
		{
			stream.write(tab);
		}
	}

}
