import sys


def pytest_collection_modifyitems(session, config, items):
    """Add last_updated to DYNAMIC_FIELDS for timestamp field handling."""
    for mod in sys.modules.values():
        df = getattr(mod, "DYNAMIC_FIELDS", None)
        if isinstance(df, set) and "created_at" in df:
            df.add("last_updated")
